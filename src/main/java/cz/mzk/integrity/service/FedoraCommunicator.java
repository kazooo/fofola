package cz.mzk.integrity.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import cz.mzk.integrity.model.FedoraDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

@Service
public class FedoraCommunicator {

    private final XMLService xmlService;
    private Client client;
    private String fedoraUrl;

    private static final Logger logger = LoggerFactory.getLogger(FedoraCommunicator.class);

    @Autowired
    public FedoraCommunicator(@Value("${spring.data.fedora.host}") String fedoraUrl,
                              @Value("${spring.data.fedora.user}") String fedoraUser,
                              @Value("${spring.data.fedora.pswd}") String fedoraPswd, XMLService xmlService) {
        this.fedoraUrl = fedoraUrl;
        client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(fedoraUser, fedoraPswd));
        this.xmlService = xmlService;
    }

    private WebResource getFedoraWebResource(String query) {
        String url = fedoraUrl + query;
        return client.resource(url);
    }


    private FedoraDocument getFedoraDoc(String uuid) throws IOException, ParserConfigurationException, SAXException {
        String query = "/objects/" + uuid + "/objectXML";
        WebResource resource = getFedoraWebResource(query);
        ClientResponse response = resource.get(ClientResponse.class);

        if (response.getStatus() == 200) {
            String docString = response.getEntity(String.class);
            if (docString == null) {
                throw new IllegalStateException("Cannot get objectXML data.");
            }
            Document doc = parseDocument(docString, true);
            return xmlService.parseFedoraDocument(doc);

        } else if (response.getStatus() == 404) {
            throw new NoSuchElementException("Can't find any Fedora document with uuid: " + uuid);
        }else {
            throw new IOException("Could not get objectXML for object " + uuid + "\nResponse status:" + response.getStatus());
        }
    }

    public FedoraDocument getFedoraDocByUuid(String uuid) {
        FedoraDocument fedoraDoc = null;
        long sleepTime= 60 * 1000; // 1 minute

        while (true) {
            try {
                fedoraDoc = getFedoraDoc(uuid);
                break;
            } catch (SAXException | ParserConfigurationException e) {
                logger.warn("Can't parse document with uuid: " + uuid);
                break;
            } catch (NoSuchElementException e) {
                break;
            } catch (IOException e) {
                logger.warn("Can't get response from Fedora, try again after 1 minute...");
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) { /* ignored */ }
            }
        }
        return fedoraDoc;
    }


    private static Document parseDocument(String xml, boolean namespaceaware)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceaware);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }
}
