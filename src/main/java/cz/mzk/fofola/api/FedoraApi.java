package cz.mzk.fofola.api;

import cz.mzk.fofola.service.RestTemplateService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;


public class FedoraApi {

    private final String fedoraHost;
    private final RestTemplate restTemplate;
    private final HttpHeaders authHeaders;
    private final HttpEntity<String> authHttpEntity;
    private final DocumentBuilder xmlParser;

    public FedoraApi(String fh, String fu, String fp) throws ParserConfigurationException {
        fedoraHost = fh;
        restTemplate = new RestTemplate();
        authHeaders = RestTemplateService.createAuthHeaders(fu, fp);
        authHttpEntity = new HttpEntity<>(authHeaders);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        xmlParser = factory.newDocumentBuilder();
    }

    public Document getByUuid(String uuid) {
        String url = fedoraHost + "/objects/" + uuid + "/objectXML";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, authHttpEntity, String.class);
        String docStr = Objects.requireNonNull(response.getBody());
        try {
            return xmlParser.parse(new InputSource(new StringReader(docStr)));
        } catch (SAXException | IOException e) {
            return null;
        }
    }
}
