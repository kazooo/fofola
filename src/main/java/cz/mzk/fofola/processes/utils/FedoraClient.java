package cz.mzk.fofola.processes.utils;

import com.squareup.okhttp.*;
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
import java.net.Proxy;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FedoraClient {

    private final String fedoraHost;
    private final OkHttpClient okHttpClient;
    private final DocumentBuilder xmlParser;
    private final Transformer xmlTransformer;

    public FedoraClient(String fedoraHost, String fedoraUser, String fedoraPswd)
            throws ParserConfigurationException, TransformerConfigurationException {
        this.fedoraHost = fedoraHost;
        okHttpClient = createAuthenticatedClient(fedoraUser, fedoraPswd);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        xmlParser = factory.newDocumentBuilder();
        xmlTransformer = TransformerFactory.newInstance().newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
    }

    private static OkHttpClient createAuthenticatedClient(final String username, final String password) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.MINUTES);
        client.setWriteTimeout(1, TimeUnit.MINUTES);
        client.setReadTimeout(1, TimeUnit.MINUTES);
        client.setConnectionPool(new ConnectionPool(1, 60000));
        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        return client;
    }

    public Document getRelsExt(String uuid) throws IOException, SAXException {
        return getDatastream(uuid, "RELS-EXT");
    }

    public Document getDc(String uuid) throws IOException, SAXException {
        return getDatastream(uuid, "DC");
    }

    private Document getDatastream(String uuid, String dsName) throws IOException, SAXException {
        String queryUrl = fedoraHost + "/get/" + uuid + "/" + dsName;
        Request request = new Request.Builder().url(queryUrl).build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Cannot get " + dsName + " data, unexpected code " + response);
        String xmlStr = Objects.requireNonNull(response.body()).string();
        response.body().close();
        return xmlParser.parse(new InputSource(new StringReader(xmlStr)));
    }

    public void setRelsExt(String uuid, Document relsExt) throws TransformerException, IOException {
        setDatastream(uuid, "RELS-EXT", relsExt,
                "X", "false", "A", "application/rdf+xml");
    }

    public void setDc(String uuid, Document dc) throws IOException, TransformerException {
        setDatastream(uuid, "DC", dc,
                "X", "false", "A", "text/xml");
    }

    private void setDatastream(String uuid, String dsName, Document doc,
                               String controlGroup, String versionable, String dsState, String mimeType)
            throws IOException, TransformerException {
        String postUrlStr = fedoraHost + "/objects/" + uuid + "/datastreams/" + dsName;
        HttpUrl postUrl = Objects.requireNonNull(HttpUrl.parse(postUrlStr)).newBuilder()
                .addQueryParameter("controlGroup", controlGroup)
                .addQueryParameter("versionable", versionable)
                .addQueryParameter("dsState", dsState)
                .addQueryParameter("mimeType", mimeType)
                .build();
        RequestBody body = RequestBody.create(MediaType.parse(mimeType), docToString(doc));

        Request request = new Request.Builder().url(postUrl).post(body).build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Cannot set " + dsName + " data for " + uuid + ", unexpected code " + response);
        }
        response.body().close();
        okHttpClient.getConnectionPool().evictAll(); // unknown problem with Fedora connection pool
    }

    private String docToString(Document doc) throws TransformerException {
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        xmlTransformer.transform(source, result);
        return result.getWriter().toString();
    }

    public void close() {
        okHttpClient.getConnectionPool().evictAll();
    }
}
