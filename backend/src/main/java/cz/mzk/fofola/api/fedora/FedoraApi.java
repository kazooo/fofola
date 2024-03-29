package cz.mzk.fofola.api.fedora;

import cz.mzk.fofola.api.utils.RestTemplateException;
import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.model.doc.Datastreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class FedoraApi {

    private final String fedoraHost;
    private final RestTemplate restTemplate;
    private final HttpEntity<String> authHttpEntity;
    private final HttpHeaders authHeaders;
    private final DocumentBuilder xmlParser;
    private final Transformer xmlTransformer;

    public FedoraApi(String fh, String fu, String fp, final RestTemplate restTemplate)
            throws ParserConfigurationException, TransformerConfigurationException {
        fedoraHost = fh;
        this.restTemplate = restTemplate;
        authHeaders = ApiConfiguration.createAuthHeaders(fu, fp);
        authHttpEntity = new HttpEntity<>(authHeaders);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        xmlParser = factory.newDocumentBuilder();
        xmlTransformer = TransformerFactory.newInstance().newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
    }

    public Document getFullFoxmlByUuid(String uuid) {
        return getFedoraResource(fedoraHost + "/objects/" + uuid + "/objectXML");
    }

    public Document getRelsExt(String uuid) {
        return getDatastream(uuid, Datastreams.RELS_EXT.name);
    }

    public Document getDc(String uuid) {
        return getDatastream(uuid, Datastreams.DC.name);
    }

    public String getTextCz(String uuid) {
        return getDatastreamContent(uuid, Datastreams.TEXT_CS.name);
    }

    public String getTextEn(String uuid) {
        return getDatastreamContent(uuid, Datastreams.TEXT_EN.name);
    }

    public String getLongTextCz(String uuid) {
        return getDatastreamContent(uuid, Datastreams.LONG_TEXT_CS.name);
    }

    public String getLongTextEn(String uuid) {
        return getDatastreamContent(uuid, Datastreams.LONG_TEXT_EN.name);
    }

    private Document getDatastream(final String uuid, final String dsName) {
        return getFedoraResource(fedoraHost + "/get/" + uuid + "/" + dsName);
    }

    private String getDatastreamContent(final String uuid, final String dsName) {
        try {
            return getFedoraTextResource(fedoraHost + "/objects/" + uuid + "/datastreams/" + dsName + "/content");
        } catch (RestTemplateException e) {
            log.warn("Error trying to get Fedora resource: " + e.getError());
        }
        return null;
    }

    private Document getFedoraResource(final String url) {
        try {
            final String docStr = Objects.requireNonNull(getFedoraTextResource(url));
            return xmlParser.parse(new InputSource(new StringReader(docStr)));
        } catch (SAXException | IOException e) {
            log.warn("XML parsing exception for " + url);
        } catch (RestTemplateException e) {
            log.warn("Error trying to get Fedora resource: " + e.getError());
        }
        return null;
    }

    private String getFedoraTextResource(final String url) {
        return restTemplate.exchange(url, HttpMethod.GET, authHttpEntity, String.class).getBody();
    }

    public void setRelsExt(String uuid, Document relsExt) throws IOException, TransformerException {
        HttpEntity<Object> entity = docStrEntity(relsExt, Datastreams.RELS_EXT.mimeType);
        setDatastream(uuid, Datastreams.RELS_EXT, entity);
    }

    public void setDc(String uuid, Document dc) throws IOException, TransformerException {
        HttpEntity<Object> entity = docStrEntity(dc, Datastreams.DC.mimeType);
        setDatastream(uuid, Datastreams.DC, entity);
    }

    public void setThumbnailImg(String uuid, MultipartFile image) throws IOException {
        HttpEntity<Object> entity = imageEntity(image, Datastreams.THUMB_IMG.mimeType);
        setDatastream(uuid, Datastreams.THUMB_IMG, entity);
    }

    public void setFullImg(String uuid, MultipartFile image) throws IOException {
        HttpEntity<Object> entity = imageEntity(image, Datastreams.FULL_IMG.mimeType);
        setDatastream(uuid, Datastreams.FULL_IMG, entity);
    }

    public void setTextEn(String uuid, String textEn) throws IOException {
        HttpEntity<Object> entity = strEntity(textEn, Datastreams.TEXT_EN.mimeType);
        setDatastream(uuid, Datastreams.TEXT_EN, entity);
    }

    public void setTextCz(String uuid, String textCz) throws IOException {
        HttpEntity<Object> entity = strEntity(textCz, Datastreams.TEXT_CS.mimeType);
        setDatastream(uuid, Datastreams.TEXT_CS, entity);
    }

    public void setLongTextCz(String uuid, String textCz) throws IOException {
        HttpEntity<Object> entity = strEntity(textCz, Datastreams.LONG_TEXT_CS.mimeType);
        setDatastream(uuid, Datastreams.LONG_TEXT_CS, entity);
    }

    public void setLongTextEn(String uuid, String textCz) throws IOException {
        HttpEntity<Object> entity = strEntity(textCz, Datastreams.LONG_TEXT_EN.mimeType);
        setDatastream(uuid, Datastreams.LONG_TEXT_EN, entity);
    }

    private HttpEntity<Object> strEntity(String str, String mimeType) {
        return new HttpEntity<>(str, authMimeTypeHeader(mimeType));
    }

    private HttpEntity<Object> docStrEntity(Document doc, String mimeType) throws TransformerException {
        return new HttpEntity<>(docToStr(doc), authMimeTypeHeader(mimeType));
    }

    private HttpEntity<Object> imageEntity(MultipartFile image, String mimeType) {
        return new HttpEntity<>(image.getResource(), authMimeTypeHeader(mimeType));
    }

    private HttpEntity<Object> formDataEntity(final MultiValueMap<Object, Object> formData, final String mimeType) {
        return new HttpEntity<>(formData, authMimeTypeHeader(mimeType));
    };

    private HttpHeaders authMimeTypeHeader(String mimeType) {
        HttpHeaders headers = new HttpHeaders(authHeaders);
        headers.setContentType(MediaType.parseMediaType(mimeType));
        return headers;
    }

    public String docToStr(Document doc) throws TransformerException {
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        xmlTransformer.transform(source, result);
        return result.getWriter().toString();
    }

    private void setDatastream(String uuid, Datastreams dataStream, HttpEntity<Object> entity) throws IOException {
        String url = fedoraHost + "/objects/" + uuid + "/datastreams/" + dataStream.name;
        Map<String, String> uriParams = new HashMap<>() {{
            put("controlGroup", dataStream.controlGroup);
            put("versionable", dataStream.versionable);
            put("dsState", dataStream.state);
            put("mimeType", dataStream.mimeType);
        }};
        url = ApiConfiguration.buildUri(url, uriParams);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (!response.getStatusCode().equals(HttpStatus.CREATED))
            throw new IOException("POST " + dataStream.name + " for " + uuid +
                    ": Cannot set datastream, unexpected code " + response.getStatusCode());
    }

    public Optional<String> queryRi(final String query) throws IOException {
        final String url = fedoraHost + "/risearch";

        final MultiValueMap<Object, Object> formData = new LinkedMultiValueMap<>() {{
            add("type", "triples");
            add("lang", "spo");
            add("format", "N-Triples");
            add("dt", "on");
            add("query", query);
        }};

        final ResponseEntity<String> response = restTemplate.postForEntity(
                url, formDataEntity(formData, MediaType.APPLICATION_FORM_URLENCODED.toString()), String.class
        );

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new IOException("POST /risearch: Cannot query Resource Index, unexpected code " + response.getStatusCode());
        }
        return Optional.ofNullable(response.getBody());
    }
}
