package cz.mzk.fofola.api;

import cz.mzk.fofola.service.RestTemplateService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;



public class FedoraApi {

    private final String fedoraHost;
    private final RestTemplate restTemplate;
    private final HttpHeaders authHeaders;
    private final HttpEntity<String> authHttpEntity;

    public FedoraApi(String fh, String fu, String fp) {
        fedoraHost = fh;
        restTemplate = new RestTemplate();
        authHeaders = RestTemplateService.createAuthHeaders(fu, fp);
        authHttpEntity = new HttpEntity<>(authHeaders);
    }

    public Document getByUuid(String uuid) {
        String url = fedoraHost + "/objects/" + uuid + "/objectXML";
        ResponseEntity<Document> response = restTemplate.exchange(url, HttpMethod.GET, authHttpEntity, Document.class);
        return response.getBody();
    }
}
