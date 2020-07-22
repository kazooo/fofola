package cz.mzk.fofola.processes.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
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
import java.net.URI;
import java.util.Collections;

public class FedoraClient {

    private final String fedoraHost;
    private final RestTemplate restTemplate;
    private final DocumentBuilder xmlParser;
    private final Transformer xmlTransformer;

    public FedoraClient(String fedoraHost, String fedoraUser, String fedoraPswd)
            throws ParserConfigurationException, TransformerConfigurationException {
        this.fedoraHost = fedoraHost;
        restTemplate = new RestTemplate(getClientHttpRequestFactory(fedoraUser, fedoraPswd));
        restTemplate.setInterceptors(Collections.singletonList(new PlusEncoderInterceptor()));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        xmlParser = factory.newDocumentBuilder();
        xmlTransformer = TransformerFactory.newInstance().newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory(String fedoraUser, String fedoraPswd) {
        int timeout = 5000;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(fedoraUser, fedoraPswd)
        );
        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    public Document getRelsExt(String uuid) throws IOException, SAXException {
        return getDatastream(uuid, FedoraUtils.DATASTREAMS.RELS_EXT.name);
    }

    public Document getDc(String uuid) throws IOException, SAXException {
        return getDatastream(uuid, FedoraUtils.DATASTREAMS.DC.name);
    }

    private Document getDatastream(String uuid, String dsName) throws IOException, SAXException {
        String getUrl = fedoraHost + "/get/" + uuid + "/" + dsName;
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK))
            throw new IOException("GET " + dsName + " for " + uuid + ": Cannot get datastream, unexpected code " + response.getStatusCode());
        if (response.getBody() == null)
            throw new IllegalStateException("GET " + dsName + " for " + uuid + ": Get request is successful, but response body is empty!");
        return xmlParser.parse(new InputSource(new StringReader(response.getBody())));
    }

    public void setRelsExt(String uuid, Document relsExt) throws IOException, TransformerException {
        HttpEntity<Object> entity = docStrEntity(relsExt, FedoraUtils.DATASTREAMS.RELS_EXT.mimeType);
        setDatastream(uuid, FedoraUtils.DATASTREAMS.RELS_EXT, entity);
    }

    public void setDc(String uuid, Document dc) throws IOException, TransformerException {
        HttpEntity<Object> entity = docStrEntity(dc, FedoraUtils.DATASTREAMS.DC.mimeType);
        setDatastream(uuid, FedoraUtils.DATASTREAMS.DC, entity);
    }

    public void setThumbnailImg(String uuid, MultipartFile image) throws IOException {
        HttpEntity<Object> entity = imageEntity(image, FedoraUtils.DATASTREAMS.THUMB_IMG.mimeType);
        setDatastream(uuid, FedoraUtils.DATASTREAMS.THUMB_IMG, entity);
    }

    public void setFullImg(String uuid, MultipartFile image) throws IOException {
        HttpEntity<Object> entity = imageEntity(image, FedoraUtils.DATASTREAMS.FULL_IMG.mimeType);
        setDatastream(uuid, FedoraUtils.DATASTREAMS.FULL_IMG, entity);
    }

    private void setDatastream(String uuid, FedoraUtils.DATASTREAMS dataStream, HttpEntity<Object> entity)
            throws IOException {
        String postUrl = UriComponentsBuilder
                .fromHttpUrl(fedoraHost + "/objects/" + uuid + "/datastreams/" + dataStream.name)
                .queryParam("controlGroup", dataStream.controlGroup)
                .queryParam("versionable", dataStream.versionable)
                .queryParam("dsState", dataStream.state)
                .queryParam("mimeType", dataStream.mimeType)
                .encode().build().toUri().toString();
        ResponseEntity<String> response = restTemplate.postForEntity(postUrl, entity, String.class);
        if (!response.getStatusCode().equals(HttpStatus.CREATED))
            throw new IOException("POST " + dataStream.name + " for " + uuid +
                    ": Cannot set datastream, unexpected code " + response.getStatusCode());
    }

    private HttpEntity<Object> docStrEntity(Document doc, String mimeType) throws TransformerException {
        return new HttpEntity<>(docToStr(doc), mimeTypeHeader(mimeType));
    }

    private HttpEntity<Object> imageEntity(MultipartFile image, String mimeType) {
        return new HttpEntity<>(image.getResource(), mimeTypeHeader(mimeType));
    }

    private HttpHeaders mimeTypeHeader(String mimeType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        return headers;
    }

    public String docToStr(Document doc) throws TransformerException {
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        xmlTransformer.transform(source, result);
        return result.getWriter().toString();
    }

    private static class PlusEncoderInterceptor implements ClientHttpRequestInterceptor {
        // https://stackoverflow.com/questions/54294843/plus-sign-not-encoded-with-resttemplate-using-string-url-but-interpreted
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            return execution.execute(new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    URI u = super.getURI();
                    String strictlyEscapedQuery = StringUtils.replace(u.getRawQuery(), "+", "%2B");
                    return UriComponentsBuilder.fromUri(u)
                            .replaceQuery(strictlyEscapedQuery)
                            .build(true).toUri();
                }
            }, body);
        }
    }
}
