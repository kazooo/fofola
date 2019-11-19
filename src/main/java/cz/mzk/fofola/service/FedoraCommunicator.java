package cz.mzk.fofola.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import cz.mzk.fofola.fedora_api.RelationshipTuple;
import cz.mzk.fofola.model.FedoraDocument;
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
import java.util.ArrayList;
import java.util.List;
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

    private FedoraDocument getFedoraDoc(String uuid) throws IOException,
            ParserConfigurationException, SAXException {
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
        long sleepTime = 60 * 1000; // 1 minute

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

    private List<RelationshipTuple> getSubjectOrObjectPids(String query) {
        List<RelationshipTuple> retval = new ArrayList<RelationshipTuple>();

        WebResource resource = getFedoraWebResource("/risearch?type=triples&lang=spo&format=N-Triples&query="
                + query);
        String result = resource.get(String.class);
        String[] lines = result.split("\n");
        for (String line : lines) {
            String[] tokens = line.split(" (?=([<*>]))");
            if (tokens.length == 2) {
                String[] rest;
                if (tokens[1].contains("<http://www.w3.org/2001/XMLSchema#dateTime>")) {
                    tokens[1] = tokens[1].replace("^^<http://www.w3.org/2001/XMLSchema#dateTime>", "");
                }
                rest = new String[2];
                rest[0] = tokens[1].substring(0, tokens[1].indexOf(" "));
                rest[1] = tokens[1].substring(tokens[1].indexOf(" ") + 1);
                String[] tokensCorrected = new String[3];
                tokensCorrected[0] = tokens[0];
                tokensCorrected[1] = rest[0];
                tokensCorrected[2] = rest[1];
                tokens = tokensCorrected;
            }
            if (tokens.length >= 3) {
                tokens[2] = tokens[2].substring(0, tokens[2].length() - 2);
            }

            if (tokens.length < 3) continue;
            try {
                RelationshipTuple tuple = new RelationshipTuple();
                tuple.setSubject(tokens[0].substring(1, tokens[0].length() - 1));
                tuple.setPredicate(tokens[1].substring(1, tokens[1].length() - 1));
                tuple.setObject(tokens[2].substring(1, tokens[2].length() - 1));
                tuple.setIsLiteral(false);
                retval.add(tuple);
            } catch (Exception ignored) { }
        }
        return retval;
    }

    private List<RelationshipTuple> getObjectPids(String subjectPid) {
        return getSubjectOrObjectPids("%3Cinfo:fedora/" + subjectPid + "%3E%20*%20*");
    }

    private ArrayList<ArrayList<String>> getAllChildren(String uuid) {
        List<RelationshipTuple> triplets = getObjectPids(uuid);
        ArrayList<ArrayList<String>> children = new ArrayList<ArrayList<String>>();

        if (triplets != null) {
            for (final RelationshipTuple triplet : triplets) {
                if (triplet.getObject().contains("uuid")
                        && triplet.getObject().contains("info:fedora/")) {

                    final String childUuid =
                            triplet.getObject().substring(("info:fedora/").length());

                    if (!childUuid.contains("/")) {
                        children.add(new ArrayList<String>() {

                            {
                                add(childUuid);
                                add(triplet.getPredicate());
                            }
                        });
                    }
                }
            }
        }
        return children;
    }

    public List<String> getChildrenUuids(String uuid) {
        List<String> uuidList = new ArrayList<>();
        ArrayList<ArrayList<String>> children = getAllChildren(uuid);
        if (children != null) {
            for (ArrayList<String> child : children) {
                uuidList.add(child.get(0));
            }
        }
        return uuidList;
    }

}
