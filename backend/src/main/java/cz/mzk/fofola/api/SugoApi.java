package cz.mzk.fofola.api;

import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.model.dnnt.*;
import cz.mzk.fofola.model.dnnt.alert.SugoAlertStats;
import cz.mzk.fofola.model.dnnt.alert.SugoRawAlertDto;
import cz.mzk.fofola.model.dnnt.alert.SugoRawAlertPreviewPageDto;
import cz.mzk.fofola.model.dnnt.job.*;
import cz.mzk.fofola.model.dnnt.session.SugoSessionDto;
import cz.mzk.fofola.model.dnnt.session.SugoSessionPageDto;
import cz.mzk.fofola.model.dnnt.transition.SugoTransitionPageDto;
import cz.mzk.fofola.rest.request.dnnt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class SugoApi {

    private final String sugoHost;
    private final RestTemplate restTemplate;

    private final static String CREATE_SESSION_ENDPOINT = "/api/session";

    private final static String DATA_QUERY_ENDPOINT = "/api/query/data/complete";
    private final static String TRANSITION_QUERY_ENDPOINT = "/api/transition";

    private final static String SESSION_ENDPOINT = "/api/session";
    private final static String JOB_ENDPOINT = "/api/job";
    private final static String ALERT_ENDPOINT = "/api/alert";

    public SugoApi(final String sugoHost, final RestTemplate restTemplate) {
        this.sugoHost = sugoHost;
        this.restTemplate = restTemplate;
    }

    public SugoDataPageDto getData(final SugoDataRequestFilter requestFilter) {
        final HttpEntity<Object> body = convertToBody(requestFilter);
        final ResponseEntity<SugoDataPageDto> response = post(DATA_QUERY_ENDPOINT, body, SugoDataPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get sessions from Sugo, response code: " + response.getStatusCode());
            return SugoDataPageDto.builder().entities(List.of()).numFound(0L).build();
        }
    }

    public SugoSessionPageDto getSessions(final SugoSessionRequestFilter requestFilter) {
        final ResponseEntity<SugoSessionPageDto> response = get(SESSION_ENDPOINT, requestFilter, SugoSessionPageDto.class);
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
        final ResponseEntity<Long> sugoResponse = post(CREATE_SESSION_ENDPOINT, body, Long.class);

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
        final ResponseEntity<Long> sugoResponse = post(CREATE_SESSION_ENDPOINT, body, Long.class);

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
        final ResponseEntity<Long> sugoResponse = post(CREATE_SESSION_ENDPOINT, body, Long.class);

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
        final ResponseEntity<Long> sugoResponse = post(CREATE_SESSION_ENDPOINT, body, Long.class);

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

    public SugoSessionDto pauseSession(String sessionId) {
        final ResponseEntity<SugoSessionDto> response =
                put(SESSION_ENDPOINT + "/" + sessionId + "/pause", null, SugoSessionDto.class);
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn(String.format(
                    "Can't pause session with id %s, response code: %s",
                    sessionId, response.getStatusCode()
            ));
            return null;
        }
    }

    public SugoSessionDto launchSession(String sessionId) {
        final ResponseEntity<SugoSessionDto> response =
                put(SESSION_ENDPOINT + "/" + sessionId + "/launch", null, SugoSessionDto.class);
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn(String.format(
                    "Can't launch session with id %s, response code: %s",
                    sessionId, response.getStatusCode()
            ));
            return null;
        }
    }

    public SugoSessionDto terminateSession(String sessionId) {
        final ResponseEntity<SugoSessionDto> response =
                put(SESSION_ENDPOINT + "/" + sessionId + "/terminate", null, SugoSessionDto.class);
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn(String.format(
                    "Can't terminate session with id %s, response code: %s",
                    sessionId, response.getStatusCode()
            ));
            return null;
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
        final ResponseEntity<SugoJobDto> response = post(JOB_ENDPOINT, body, SugoJobDto.class);

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
        final ResponseEntity<SugoJobDto> response = put(JOB_ENDPOINT, body, SugoJobDto.class);

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
                put(JOB_ENDPOINT + "/" + jobId + "/toggle", null, SugoJobDto.class);

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

    public SugoJobDto triggerJob(String jobId) {
        final ResponseEntity<SugoJobDto> response =
                put(JOB_ENDPOINT + "/" + jobId + "/trigger", null, SugoJobDto.class);

        final HttpStatus statusCode = response.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK) && !statusCode.equals(HttpStatus.ACCEPTED)) {
            log.warn(String.format(
                    "Can't trigger an automatic job with id \"%s\", response code: %s",
                    jobId, response.getStatusCode()
            ));
            return new SugoJobDto();
        } else {
            return response.getBody();
        }
    }

    public SugoRawAlertPreviewPageDto getAlertPreviews(final SugoAlertFilter filter) {
        final ResponseEntity<SugoRawAlertPreviewPageDto> response = get(ALERT_ENDPOINT, filter, SugoRawAlertPreviewPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get alerts from Sugo, response code: " + response.getStatusCode());
            return SugoRawAlertPreviewPageDto.builder().entities(List.of()).numFound(0L).build();
        }
    }

    public SugoRawAlertDto getAlert(final String alertId) {
        final ResponseEntity<SugoRawAlertDto> response = get(ALERT_ENDPOINT + "/" + alertId, null, SugoRawAlertDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn(String.format(
                    "Can't get alert with id \"%s\" from Sugo, response code: %s",
                    alertId, response.getStatusCode()
            ));
            return SugoRawAlertDto.builder().build();
        }
    }

    public SugoRawAlertDto solveAlert(final String alertId) {
        final ResponseEntity<SugoRawAlertDto> response = put(ALERT_ENDPOINT + "/" + alertId + "/solve", null, SugoRawAlertDto.class);
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn(String.format(
                    "Can't solve alert with id %s, response code: %s",
                    alertId, response.getStatusCode()
            ));
            return null;
        }
    }

    public SugoAlertStats getAlertStats() {
        final ResponseEntity<SugoAlertStats> response = get(ALERT_ENDPOINT + "/stats", null, SugoAlertStats.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get alert stats from Sugo, response code: " + response.getStatusCode());
            return SugoAlertStats.builder().build();
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

    private <T> ResponseEntity<T> post(final String url, final Object body, final Class<T> responseClass) {
        return restTemplate.postForEntity(sugoHost + url, body, responseClass);
    }

    private <T> ResponseEntity<T> put(final String url, final HttpEntity<Object> body, final Class<T> responseClass) {
        return restTemplate.exchange(sugoHost + url, HttpMethod.PUT, body, responseClass);
    }

    private <T> ResponseEntity<T> delete(final String url, final Class<T> responseClass) {
        return restTemplate.exchange(sugoHost + url, HttpMethod.DELETE, null, responseClass);
    }
}
