package cz.mzk.fofola.api;

import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.model.dnnt.SugoDataPageDto;
import cz.mzk.fofola.model.dnnt.SugoMarkParams;
import cz.mzk.fofola.model.dnnt.SugoSessionPageDto;
import cz.mzk.fofola.model.dnnt.SugoTransitionPageDto;
import cz.mzk.fofola.rest.request.dnnt.SugoDataRequestFilter;
import cz.mzk.fofola.rest.request.dnnt.SugoSessionRequestFilter;
import cz.mzk.fofola.rest.request.dnnt.SugoTransitionRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
public class SugoApi {

    private final String sugoHost;
    private final RestTemplate restTemplate;

    private final static String MARK_ENDPOINT = "/api/command/mark";
    private final static String UNMARK_ENDPOINT = "/api/command/unmark";
    private final static String SYNC_ENDPOINT = "/api/command/sync";
    private final static String CLEAN_ENDPOINT = "/api/command/clean";

    private final static String DATA_QUERY_ENDPOINT = "/api/query/data/complete";
    private final static String SESSION_QUERY_ENDPOINT = "/api/query/session";
    private final static String TRANSITION_QUERY_ENDPOINT = "/api/query/transition";

    public SugoApi(final String sugoHost, final RestTemplate restTemplate) {
        this.sugoHost = sugoHost;
        this.restTemplate = restTemplate;
    }

    public SugoDataPageDto getData(final SugoDataRequestFilter requestFilter) {
        final String url = buildUrl(DATA_QUERY_ENDPOINT, null);
        final HttpEntity<Object> body = convertToBody(requestFilter);
        final ResponseEntity<SugoDataPageDto> response = send(url, body, SugoDataPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get sessions from Sugo, response code: " + response.getStatusCode());
            return SugoDataPageDto.builder().entities(List.of()).numFound(0L).build();
        }
    }

    public SugoSessionPageDto getSessions(final SugoSessionRequestFilter requestFilter) {
        final String url = buildUrl(SESSION_QUERY_ENDPOINT, requestFilter);
        final ResponseEntity<SugoSessionPageDto> response = restTemplate.getForEntity(url, SugoSessionPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get sessions from Sugo, response code: " + response.getStatusCode());
            return SugoSessionPageDto.builder().entities(List.of()).numFound(0L).build();
        }
    }

    public SugoTransitionPageDto getTransitions(final SugoTransitionRequestFilter requestFilter) {
        final String url = buildUrl(TRANSITION_QUERY_ENDPOINT, requestFilter);
        final ResponseEntity<SugoTransitionPageDto> response = restTemplate.getForEntity(url, SugoTransitionPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get transitions from Sugo, response code: " + response.getStatusCode());
            return SugoTransitionPageDto.builder().entities(List.of()).numFound(0L).build();
        }
    }

    public Long mark(final SugoMarkParams params, final List<String> uuids) {
        final String url = buildUrl(MARK_ENDPOINT, params);
        final HttpEntity<Object> body = convertToBody(uuids);
        final ResponseEntity<Long> sugoResponse = send(url, body, Long.class);

        if (!sugoResponse.getStatusCode().equals(HttpStatus.ACCEPTED)) {
            log.warn(String.format(
                    "Can't send %d uuids to %s mark by %s label!",
                    uuids.size(),
                    params.getRecursively() ? "recursively" : "non-recursively",
                    params.getLabel().getValue()
            ));
            return -1L;
        } else {
            return sugoResponse.getBody();
        }
    }

    public Long unmark(final SugoMarkParams params, final List<String> uuids) {
        final String url = buildUrl(UNMARK_ENDPOINT, params);
        final HttpEntity<Object> body = convertToBody(uuids);
        final ResponseEntity<Long> sugoResponse = send(url, body, Long.class);

        if (!sugoResponse.getStatusCode().equals(HttpStatus.ACCEPTED)) {
            log.warn(String.format(
                    "Can't send %d uuids to %s unmark by %s label!",
                    uuids.size(),
                    params.getRecursively() ? "recursively" : "non-recursively",
                    params.getLabel().getValue()
            ));
            return -1L;
        } else {
            return sugoResponse.getBody();
        }
    }

    public Long sync(final List<String> uuids) {
        final String url = buildUrl(SYNC_ENDPOINT, null);
        final HttpEntity<Object> body = convertToBody(uuids);
        final ResponseEntity<Long> sugoResponse = send(url, body, Long.class);

        if (!sugoResponse.getStatusCode().equals(HttpStatus.ACCEPTED)) {
            log.warn(String.format("Can't send %d uuids to synchronize with DNNT source!", uuids.size()));
            return -1L;
        } else {
            return sugoResponse.getBody();
        }
    }

    public Long clean(final SugoMarkParams params, final List<String> uuids) {
        final String url = buildUrl(CLEAN_ENDPOINT, params);
        final HttpEntity<Object> body = convertToBody(uuids);
        final ResponseEntity<Long> sugoResponse = send(url, body, Long.class);

        if (!sugoResponse.getStatusCode().equals(HttpStatus.ACCEPTED)) {
            log.warn(String.format(
                    "Can't send %d uuids to %s clean all labels!",
                    uuids.size(),
                    params.getRecursively() ? "recursively" : "non-recursively"
            ));
            return -1L;
        } else {
            return sugoResponse.getBody();
        }
    }

    private String buildUrl(final String endpoint, final Object urlParams) {
        if (urlParams != null) {
            return ApiConfiguration.buildUri(sugoHost + "/" + endpoint, urlParams);
        } else {
            return sugoHost + "/" + endpoint;
        }
    }

    private HttpEntity<Object> convertToBody(final Object object) {
        return new HttpEntity<>(object);
    }

    private <T> ResponseEntity<T> send(final String url, final Object body, final Class<T> responseClass) {
        return restTemplate.postForEntity(url, body, responseClass);
    }
}
