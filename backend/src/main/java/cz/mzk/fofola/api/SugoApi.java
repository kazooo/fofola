package cz.mzk.fofola.api;

import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.enums.dnnt.DnntLabel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SugoApi {

    private final String sugoHost;
    private final RestTemplate restTemplate;

    private final static String MARK_ENDPOINT = "/api/command/mark";
    private final static String UNMARK_ENDPOINT = "/api/command/unmark";
    private final static String SYNC_ENDPOINT = "/api/command/sync";
    private final static String CLEAN_ENDPOINT = "/api/command/clean";

    public SugoApi(final String sugoHost, final RestTemplate restTemplate) {
        this.sugoHost = sugoHost;
        this.restTemplate = restTemplate;
    }

    public void mark(final DnntLabel label, final Boolean recursively, final List<String> uuids) {
        final Map<String, String> urlParams = createParams(label, recursively);
        final String url = buildUrl(MARK_ENDPOINT, urlParams);
        final HttpEntity<Object> body = convertToBody(uuids);
        if (!sendSuccess(url, body)) {
            log.warn(String.format(
                    "Can't send %d uuids to %s mark by %s label!",
                    uuids.size(), recursively ? "recursively" : "non-recursively", label.getValue()
            ));
        }
    }

    public void unmark(final DnntLabel label, final Boolean recursively, final List<String> uuids) {
        final Map<String, String> urlParams = createParams(label, recursively);
        final String url = buildUrl(UNMARK_ENDPOINT, urlParams);
        final HttpEntity<Object> body = convertToBody(uuids);
        if (!sendSuccess(url, body)) {
            log.warn(String.format(
                    "Can't send %d uuids to %s unmark by %s label!",
                    uuids.size(), recursively ? "recursively" : "non-recursively", label.getValue()
            ));
        }
    }

    public void sync(final List<String> uuids) {
        final String url = buildUrl(SYNC_ENDPOINT, null);
        final HttpEntity<Object> body = convertToBody(uuids);
        if (!sendSuccess(url, body)) {
            log.warn(String.format("Can't send %d uuids to synchronize with DNNT source!", uuids.size()));
        }
    }

    public void clean(final Boolean recursively, final List<String> uuids) {
        final Map<String, String> urlParams = createParams(null, recursively);
        final String url = buildUrl(CLEAN_ENDPOINT, urlParams);
        final HttpEntity<Object> body = convertToBody(uuids);
        if (!sendSuccess(url, body)) {
            log.warn(String.format(
                    "Can't send %d uuids to %s clean all labels!",
                    uuids.size(), recursively ? "recursively" : "non-recursively"
            ));
        }
    }

    private Map<String, String> createParams(final DnntLabel label, final Boolean recursively) {
        final Map<String, String> urlParams = new HashMap<>();
        if (label != null) {
            urlParams.put("label", label.getValue());
        }
        if (recursively != null) {
            urlParams.put("recursive", recursively.toString());
        }
        return urlParams;
    }

    private String buildUrl(final String endpoint, final Map<String, String> urlParams) {
        if (urlParams != null) {
            return ApiConfiguration.buildUri(sugoHost + "/" + endpoint, urlParams);
        } else {
            return sugoHost + "/" + endpoint;
        }
    }

    private HttpEntity<Object> convertToBody(final List<String> uuids) {
        return new HttpEntity<>(uuids);
    }

    private boolean sendSuccess(final String url, final Object body) {
        final ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }
}
