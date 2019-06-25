package cz.mzk.integrity.researcher;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

@Component
public class FedoraCommunicator {

    private String fedoraUrl;

    private Client client;

    public FedoraCommunicator(@Value("${spring.data.fedora.host}") String fedoraUrl,
                              @Value("${spring.data.fedora.user}") String fedoraUser,
                              @Value("${spring.data.fedora.pswd}") String fedoraPswd) {
        this.fedoraUrl = fedoraUrl;
        client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(fedoraUser, fedoraPswd));
    }

    private WebResource getFedoraWebResource(String query) {
        String url = fedoraUrl + query;
        return client.resource(url);
    }


    public Document getFedoraDocByUuid(String uuid) throws IOException, ParserConfigurationException, SAXException {
        String query = "/objects/" + uuid + "/objectXML";
        WebResource resource = getFedoraWebResource(query);
        ClientResponse response = resource.get(ClientResponse.class);

        if (response.getStatus() == 200) {
            String docString = response.getEntity(String.class);
            if (docString == null) {
                throw new IllegalStateException("Cannot get objectXML data.");
            }
            return parseDocument(docString, true);
        } else if (response.getStatus() == 404) {
            throw new NoSuchElementException("Can't find any Fedora document with uuid: " + uuid);
        }else {
            throw new IOException("Could not get objectXML for object " + uuid + "\nResponse status:" + response.getStatus());
        }
    }

    private static Document parseDocument(String xml, boolean namespaceaware)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceaware);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }
}
