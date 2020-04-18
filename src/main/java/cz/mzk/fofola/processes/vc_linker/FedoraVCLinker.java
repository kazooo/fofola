package cz.mzk.fofola.processes.vc_linker;

import com.squareup.okhttp.*;
import org.w3c.dom.*;
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


/**
 * This linker load RELS-EXT data stream for each uuid and creates link to virtual collection
 * by adding XML element with virtual collection ID to mentioned data stream.
 * At the end it pushes new data stream to Fedora. New triplet for virtual collection link
 * is created automatically by Fedora.
 *
 * @author Aleksei Ermak
 */
public class FedoraVCLinker {

    private final String fedoraHost;
    private final OkHttpClient fedoraClient;
    private final DocumentBuilder xmlParser;
    private final Transformer xmlTransformer;

    public FedoraVCLinker(String fedoraHost, String fedoraUser, String fedoraPswd)
            throws ParserConfigurationException, TransformerConfigurationException {
        this.fedoraHost = fedoraHost;
        fedoraClient = createAuthenticatedClient(fedoraUser, fedoraPswd);

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

    public boolean writeVCFor(String uuid, String vcId) throws TransformerException, IOException, SAXException {
        Document relsExt = getRelsExt(uuid);
        if (relsExt == null) return false;
        Element descriptionElement = (Element) relsExt.getElementsByTagName("rdf:Description").item(0);
        NodeList collectionNodes = descriptionElement.getElementsByTagName("rdf:isMemberOfCollection");
        String vcIdElementAttrName = "info:fedora/" + vcId;
        boolean canBeAdded = collectionNodes == null || !containsVC(collectionNodes, vcIdElementAttrName);
        if (canBeAdded) {
            Element childElement = relsExt.createElement("rdf:isMemberOfCollection");
            childElement.setAttribute("rdf:resource", vcIdElementAttrName);
            descriptionElement.appendChild(childElement);
            setRelsExt(uuid, relsExt);
        }
        return canBeAdded;
    }

    private boolean containsVC(NodeList collectionNodes, String vcIdElementAttrName) {
        for (int i = 0; i < collectionNodes.getLength(); i++) {
            Node collectionNode = collectionNodes.item(i);
            NamedNodeMap attributes = collectionNode.getAttributes();
            if (attributes.getLength() < 1) {
                continue;
            }
            Node attribute = attributes.getNamedItem("rdf:resource");
            if (attribute.getTextContent().equals(vcIdElementAttrName)) {
                return true;
            }
        }
        return false;
    }

    private Document getRelsExt(String uuid) throws IOException, SAXException {
        String queryUrl = fedoraHost + "/get/" + uuid + "/RELS-EXT";
        Request request = new Request.Builder().url(queryUrl).build();
        Response response = fedoraClient.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Cannot get RELS EXT data, unexpected code " + response);
        String xmlStr = Objects.requireNonNull(response.body()).string();
        response.body().close();
        return xmlParser.parse(new InputSource(new StringReader(xmlStr)));
    }

    @SuppressWarnings("deprecation")
    private void setRelsExt(String uuid, Document relsExt) throws TransformerException, IOException {
        String postUrlStr = fedoraHost + "/objects/" + uuid + "/datastreams/RELS-EXT";
        String mimeType = "application/rdf+xml";
        String versionable = "true";
        String controlGroup = "X";
        String dsState = "A";

        HttpUrl postUrl = Objects.requireNonNull(HttpUrl.parse(postUrlStr)).newBuilder()
                .addQueryParameter("controlGroup", controlGroup)
                .addQueryParameter("versionable", versionable)
                .addQueryParameter("dsState", dsState)
                .addQueryParameter("mimeType", mimeType)
                .build();
        RequestBody body = RequestBody.create(MediaType.parse(mimeType), docToString(relsExt));

        Request request = new Request.Builder().url(postUrl).post(body).build();
        Response response = fedoraClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Cannot set RELS EXT data for " + uuid + ", unexpected code " + response);
        }
        response.body().close();
        fedoraClient.getConnectionPool().evictAll(); // unknown problem with Fedora connection pool
    }

    private String docToString(Document doc) throws TransformerException {
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        xmlTransformer.transform(source, result);
        return result.getWriter().toString();
    }

    public void close() {
        fedoraClient.getConnectionPool().evictAll();
    }
}

