package cz.mzk.fofola.api;

import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.model.dnnt.SugoDataPageDto;
import cz.mzk.fofola.model.dnnt.SugoMarkParams;
import cz.mzk.fofola.model.dnnt.SugoSessionPageDto;
import cz.mzk.fofola.model.dnnt.SugoTransitionPageDto;
import cz.mzk.fofola.model.dnnt.job.CreateSugoJobDto;
import cz.mzk.fofola.model.dnnt.job.SugoJobDto;
import cz.mzk.fofola.model.dnnt.job.SugoJobPreviewPageDto;
import cz.mzk.fofola.model.dnnt.job.UpdateSugoJobDto;
import cz.mzk.fofola.rest.request.dnnt.SugoDataRequestFilter;
import cz.mzk.fofola.rest.request.dnnt.SugoJobFilter;
import cz.mzk.fofola.rest.request.dnnt.SugoSessionRequestFilter;
import cz.mzk.fofola.rest.request.dnnt.SugoTransitionRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
public class SugoApi {

    private final String sugoHost;
    private final RestTemplate restTemplate;

    private final static String CREATE_SESSION_ENDPOINT = "/api/session";

    private final static String DATA_QUERY_ENDPOINT = "/api/query/data/complete";
    private final static String SESSION_QUERY_ENDPOINT = "/api/session";
    private final static String TRANSITION_QUERY_ENDPOINT = "/api/transition";

    private final static String JOB_ENDPOINT = "/api/job";

    public SugoApi(final String sugoHost, final RestTemplate restTemplate) {
        this.sugoHost = sugoHost;
        this.restTemplate = restTemplate;
    }

    public SugoDataPageDto getData(final SugoDataRequestFilter requestFilter) {
        final HttpEntity<Object> body = convertToBody(requestFilter);
        final ResponseEntity<SugoDataPageDto> response = create(DATA_QUERY_ENDPOINT, body, SugoDataPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get sessions from Sugo, response code: " + response.getStatusCode());
            return SugoDataPageDto.builder().entities(List.of()).numFound(0L).build();
        }
    }

    public SugoSessionPageDto getSessions(final SugoSessionRequestFilter requestFilter) {
        final ResponseEntity<SugoSessionPageDto> response = get(SESSION_QUERY_ENDPOINT, requestFilter, SugoSessionPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get sessions from Sugo, response code: " + response.getStatusCode());
            return SugoSessionPageDto.builder().entities(List.of()).numFound(0L).build();
        }
    }

    public SugoTransitionPageDto getTransitions(final SugoTransitionRequestFilter requestFilter) {
        final ResponseEntity<SugoTransitionPageDto> response =
                get(TRANSITION_QUERY_ENDPOINT, requestFilter, SugoTransitionPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get transitions from Sugo, response code: " + response.getStatusCode());
            return SugoTransitionPageDto.builder().entities(List.of()).numFound(0L).build();
        }
    }

    public Long mark(final SugoMarkParams params) {
        final HttpEntity<Object> body = convertToBody(params);
        final ResponseEntity<Long> sugoResponse = create(CREATE_SESSION_ENDPOINT, body, Long.class);

        final HttpStatus statusCode = sugoResponse.getStatusCode();
        if (!(statusCode.equals(HttpStatus.ACCEPTED) || statusCode.equals(HttpStatus.CREATED))) {
            log.warn(String.format(
                    "Can't send %d uuids to %s mark by %s label!",
                    params.getUuids().size(),
                    params.getRecursively() ? "recursively" : "non-recursively",
                    params.getLabel().getValue()
            ));
            return -1L;
        } else {
            return sugoResponse.getBody();
        }
    }

    public Long unmark(final SugoMarkParams params) {
        final HttpEntity<Object> body = convertToBody(params);
        final ResponseEntity<Long> sugoResponse = create(CREATE_SESSION_ENDPOINT, body, Long.class);

        final HttpStatus statusCode = sugoResponse.getStatusCode();
        if (!(statusCode.equals(HttpStatus.ACCEPTED) || statusCode.equals(HttpStatus.CREATED))) {
            log.warn(String.format(
                    "Can't send %d uuids to %s unmark by %s label!",
                    params.getUuids().size(),
                    params.getRecursively() ? "recursively" : "non-recursively",
                    params.getLabel().getValue()
            ));
            return -1L;
        } else {
            return sugoResponse.getBody();
        }
    }

    public Long sync(final SugoMarkParams params) {
        final HttpEntity<Object> body = convertToBody(params);
        final ResponseEntity<Long> sugoResponse = create(CREATE_SESSION_ENDPOINT, body, Long.class);

        final HttpStatus statusCode = sugoResponse.getStatusCode();
        if (!(statusCode.equals(HttpStatus.ACCEPTED) || statusCode.equals(HttpStatus.CREATED))) {
            log.warn(String.format("Can't send %d uuids to synchronize with DNNT source!", params.getUuids().size()));
            return -1L;
        } else {
            return sugoResponse.getBody();
        }
    }

    public Long clean(final SugoMarkParams params) {
        final HttpEntity<Object> body = convertToBody(params);
        final ResponseEntity<Long> sugoResponse = create(CREATE_SESSION_ENDPOINT, body, Long.class);

        final HttpStatus statusCode = sugoResponse.getStatusCode();
        if (!(statusCode.equals(HttpStatus.ACCEPTED) || statusCode.equals(HttpStatus.CREATED))) {
            log.warn(String.format(
                    "Can't send %d uuids to %s clean all labels!",
                    params.getUuids().size(),
                    params.getRecursively() ? "recursively" : "non-recursively"
            ));
            return -1L;
        } else {
            return sugoResponse.getBody();
        }
    }

    public SugoJobPreviewPageDto getJobPreviews(final SugoJobFilter filter) {
        final ResponseEntity<SugoJobPreviewPageDto> response = get(JOB_ENDPOINT, filter, SugoJobPreviewPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get automatic jobs from Sugo, response code: " + response.getStatusCode());
            return SugoJobPreviewPageDto.builder().entities(List.of()).numFound(0L).build();
        }
    }

    public SugoJobDto getJob(final String jobId) {
        final ResponseEntity<SugoJobDto> response = get(JOB_ENDPOINT + "/" + jobId, null, SugoJobDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn(String.format(
                    "Can't get automatic job with id \"%s\" from Sugo, response code: %s",
                    jobId, response.getStatusCode()
            ));
            return SugoJobDto.builder().build();
        }
    }

    public SugoJobDto createJob(final CreateSugoJobDto createJob) {
        final HttpEntity<Object> body = convertToBody(createJob);
        final ResponseEntity<SugoJobDto> response = create(JOB_ENDPOINT, body, SugoJobDto.class);

        final HttpStatus statusCode = response.getStatusCode();
        if (!statusCode.equals(HttpStatus.CREATED)) {
            log.warn("Can't create a new automatic job, response code: " + response.getStatusCode());
            return new SugoJobDto();
        } else {
            return response.getBody();
        }
    }

    public SugoJobDto updateJob(final UpdateSugoJobDto updateJob) {
        final HttpEntity<Object> body = convertToBody(updateJob);
        final ResponseEntity<SugoJobDto> response = update(JOB_ENDPOINT, body, SugoJobDto.class);

        final HttpStatus statusCode = response.getStatusCode();
        if (!statusCode.equals(HttpStatus.ACCEPTED)) {
            log.warn(String.format(
                    "Can't update an automatic job with id \"%s\", response code: %s",
                    updateJob.getId(), response.getStatusCode()
            ));
            return new SugoJobDto();
        } else {
            return response.getBody();
        }
    }

    public SugoJobDto deleteJob(final String jobId) {
        final ResponseEntity<SugoJobDto> response = delete(JOB_ENDPOINT + "/" + jobId, SugoJobDto.class);

        final HttpStatus statusCode = response.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)) {
            log.warn(String.format(
                    "Can't delete an automatic job with id \"%s\", response code: %s",
                    jobId, response.getStatusCode()
            ));
            return new SugoJobDto();
        } else {
            return response.getBody();
        }
    }

    public SugoJobDto toggleJob(final String jobId) {
        final ResponseEntity<SugoJobDto> response =
                update(JOB_ENDPOINT + "/" + jobId + "/toggle", null, SugoJobDto.class);

        final HttpStatus statusCode = response.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK) && !statusCode.equals(HttpStatus.ACCEPTED)) {
            log.warn(String.format(
                    "Can't toggle an automatic job with id \"%s\", response code: %s",
                    jobId, response.getStatusCode()
            ));
            return new SugoJobDto();
        } else {
            return response.getBody();
        }
    }

    private HttpEntity<Object> convertToBody(final Object object) {
        return new HttpEntity<>(object);
    }

    private <T> ResponseEntity<T> get(final String url, final Object urlParams, final Class<T> responseClass) {
        final String finalUrl;
        if (urlParams != null) {
            finalUrl = ApiConfiguration.buildUri(sugoHost + url, urlParams);
        } else {
            finalUrl = sugoHost + url;
        }
        return restTemplate.getForEntity(finalUrl, responseClass);
    }

    private <T> ResponseEntity<T> create(final String url, final Object body, final Class<T> responseClass) {
        return restTemplate.postForEntity(sugoHost + url, body, responseClass);
    }

    private <T> ResponseEntity<T> update(final String url, final HttpEntity<Object> body, final Class<T> responseClass) {
        return restTemplate.exchange(sugoHost + url, HttpMethod.PUT, body, responseClass);
    }

    private <T> ResponseEntity<T> delete(final String url, final Class<T> responseClass) {
        return restTemplate.exchange(sugoHost + url, HttpMethod.DELETE, null, responseClass);
    }
}
