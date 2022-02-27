package cz.mzk.fofola.api;

import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.model.dnnt.SugoMarkParams;
import cz.mzk.fofola.model.dnnt.SugoSessionPageDto;
import cz.mzk.fofola.rest.request.SugoSessionRequestFilter;
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

    private final static String SESSION_QUERY_ENDPOINT = "/api/query/session";

    public SugoApi(final String sugoHost, final RestTemplate restTemplate) {
        this.sugoHost = sugoHost;
        this.restTemplate = restTemplate;
    }

    public SugoSessionPageDto getSessions(final SugoSessionRequestFilter requestFilter) {
        final String url = buildUrl(SESSION_QUERY_ENDPOINT, requestFilter);
        final ResponseEntity<SugoSessionPageDto> response = restTemplate.getForEntity(url, SugoSessionPageDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody());
        } else {
            log.warn("Can't get sessions from Sugo, response code: " + response.getStatusCode());
            return SugoSessionPageDto.builder().sessions(List.of()).numFound(0L).build();
        }
    }

    public void mark(final SugoMarkParams params, final List<String> uuids) {
        final String url = buildUrl(MARK_ENDPOINT, params);
        final HttpEntity<Object> body = convertToBody(uuids);
        if (sendFailed(url, body)) {
            log.warn(String.format(
                    "Can't send %d uuids to %s mark by %s label!",
                    uuids.size(),
                    params.getRecursively() ? "recursively" : "non-recursively",
                    params.getLabel().getValue()
            ));
        }
    }

    public void unmark(final SugoMarkParams params, final List<String> uuids) {
        final String url = buildUrl(UNMARK_ENDPOINT, params);
        final HttpEntity<Object> body = convertToBody(uuids);
        if (sendFailed(url, body)) {
            log.warn(String.format(
                    "Can't send %d uuids to %s unmark by %s label!",
                    uuids.size(),
                    params.getRecursively() ? "recursively" : "non-recursively",
                    params.getLabel().getValue()
            ));
        }
    }

    public void sync(final List<String> uuids) {
        final String url = buildUrl(SYNC_ENDPOINT, null);
        final HttpEntity<Object> body = convertToBody(uuids);
        if (sendFailed(url, body)) {
            log.warn(String.format("Can't send %d uuids to synchronize with DNNT source!", uuids.size()));
        }
    }

    public void clean(final SugoMarkParams params, final List<String> uuids) {
        final String url = buildUrl(CLEAN_ENDPOINT, params);
        final HttpEntity<Object> body = convertToBody(uuids);
        if (sendFailed(url, body)) {
            log.warn(String.format(
                    "Can't send %d uuids to %s clean all labels!",
                    uuids.size(),
                    params.getRecursively() ? "recursively" : "non-recursively"
            ));
        }
    }

    private String buildUrl(final String endpoint, final Object urlParams) {
        if (urlParams != null) {
            return ApiConfiguration.buildUri(sugoHost + "/" + endpoint, urlParams);
        } else {
            return sugoHost + "/" + endpoint;
        }
    }

    private HttpEntity<Object> convertToBody(final List<String> uuids) {
        return new HttpEntity<>(uuids);
    }

    private boolean sendFailed(final String url, final Object body) {
        final ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);
        return !response.getStatusCode().equals(HttpStatus.ACCEPTED);
    }
}
