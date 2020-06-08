package cz.mzk.fofola.processes.utils;

import com.squareup.okhttp.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Proxy;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FedoraClient {

    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPassword;
    private final OkHttpClient okHttpClient;
    private final DocumentBuilder xmlParser;
    private final Transformer xmlTransformer;
    private final Client relsExtClient;

    public FedoraClient(String fedoraHost, String fedoraUser, String fedoraPswd)
            throws ParserConfigurationException, TransformerConfigurationException {
        this.fedoraHost = fedoraHost;
        this.fedoraUser = fedoraUser;
        this.fedoraPassword = fedoraPswd;
        okHttpClient = createAuthenticatedClient(fedoraUser, fedoraPswd);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        xmlParser = factory.newDocumentBuilder();
        xmlTransformer = TransformerFactory.newInstance().newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        relsExtClient = Client.create();
        relsExtClient.setConnectTimeout(600000);
        relsExtClient.setReadTimeout(600000);
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
        String url = fedoraHost + "/objects/" + uuid + "/datastreams/RELS-EXT";
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("controlGroup", "X");
        queryParams.add("versionable", "false");
        queryParams.add("dsState", "A");
        queryParams.add("mimeType", "application/rdf+xml");
        File tempDom = File.createTempFile("relsExt", ".rdf");
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(relsExt), new StreamResult(tempDom));

        WebResource resource = relsExtClient.resource(url);
        BasicAuthenticationFilter credentials = new BasicAuthenticationFilter(fedoraUser, fedoraPassword);
        resource.addFilter(credentials);
        try {
            resource.queryParams(queryParams).post(ClientResponse.class, tempDom);
        } catch (UniformInterfaceException e) {
            int status = e.getResponse().getStatus();
            throw new IOException("Cannot set RELS-EXT data for " + uuid + ", status: " + status);
        }
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
        relsExtClient.destroy();
        okHttpClient.getConnectionPool().evictAll();
    }
}
