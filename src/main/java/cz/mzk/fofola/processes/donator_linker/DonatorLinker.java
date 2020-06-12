package cz.mzk.fofola.processes.donator_linker;

import cz.mzk.fofola.processes.utils.FedoraClient;
import cz.mzk.fofola.processes.utils.FedoraUtils;
import cz.mzk.fofola.processes.utils.SolrUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DonatorLinker {

    private final SolrClient solrClient;
    private final FedoraClient fedoraClient;
    private final int maxDocsPerQuery;
    private final Logger logger;

    public DonatorLinker(String fedoraHost, String fedoraUser, String fedoraPswd,
                         String solrHost, int maxDocsPerQuery, Logger logger)
            throws ParserConfigurationException, TransformerConfigurationException {
        solrClient = SolrUtils.buildClient(solrHost);
        fedoraClient = new FedoraClient(fedoraHost, fedoraUser, fedoraPswd);
        this.logger = logger;
        this.maxDocsPerQuery = maxDocsPerQuery;
    }

    public void link(String rootUuid, String donator) throws IOException, SolrServerException {
        SolrQuery query = createQueryForRootUuid(rootUuid);
        Consumer<SolrDocument> donatorLinkingLogic = solrDoc -> {
            String docPID = (String) solrDoc.getFieldValue("PID");
            logger.info(docPID);
            try {
                Document relsExt = fedoraClient.getRelsExt(docPID);
                Node descriptionRootNode = getDescRootNode(relsExt);
                if (getHasDonatorNode(donator, descriptionRootNode) == null) {
                    insertHasDonatorNode(donator, relsExt, descriptionRootNode);
                    fedoraClient.setRelsExt(docPID, relsExt);
                }
            } catch (IOException | SAXException | TransformerException e) {
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        };
        SolrUtils.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                query, solrClient, donatorLinkingLogic, maxDocsPerQuery
        );
    }

    public void unlink(String rootUuid, String donator) throws IOException, SolrServerException {
        SolrQuery query = createQueryForRootUuid(rootUuid);
        Consumer<SolrDocument> donatorUnlinkingLogic = solrDoc -> {
            String docPID = (String) solrDoc.getFieldValue("PID");
            try {
                Document relsExt = fedoraClient.getRelsExt(docPID);
                Node descriptionRootNode = getDescRootNode(relsExt);
                Node hasDonatorNode = getHasDonatorNode(donator, descriptionRootNode);
                if (hasDonatorNode != null) {
                    hasDonatorNode.getParentNode().removeChild(hasDonatorNode);
                    fedoraClient.setRelsExt(docPID, relsExt);
                }
            } catch (IOException | SAXException | TransformerException e) {
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        };
        SolrUtils.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                query, solrClient, donatorUnlinkingLogic, maxDocsPerQuery
        );
    }

    private static SolrQuery createQueryForRootUuid(String rootUuid) {
        String allDocsQueryStr = "root_pid:\"" + rootUuid.trim() + "\"";
        SolrQuery query = new SolrQuery(allDocsQueryStr);
        query.addField("PID");
        return query;
    }

    private static void insertHasDonatorNode(String donator, Document relsExt, Node descriptionRootNode) {
        Element hasDonatorElement = relsExt.createElement("hasDonator");
        hasDonatorElement.setAttribute("xmlns", "http://www.nsdl.org/ontologies/relationships#");
        hasDonatorElement.setAttribute("rdf:resource", "info:fedora/donator:" + donator);
        descriptionRootNode.appendChild(hasDonatorElement);
    }

    private static Node getHasDonatorNode(String donator, Node descriptionRootNode) {
        AtomicReference<Node> hasDonatorNode = new AtomicReference<>();
        FedoraUtils.iterateChildNodes(descriptionRootNode, node -> {
            if (node.getNodeName().equals("hasDonator")) {
                Attr donatorAttr = FedoraUtils.getAttributeWithName(node, "rdf:resource");
                if (donatorAttr != null && donatorAttr.getValue().contains(donator)) {
                    hasDonatorNode.set(node);
                }
            }
        });
        return hasDonatorNode.get();
    }

    private static Node getDescRootNode(Document relsExt) {
        return FedoraUtils.getFirstNode(
                relsExt.getDocumentElement(), "rdf:Description"
        );
    }

    public void commitAndClose() throws IOException, SolrServerException {
        solrClient.commit();
        solrClient.close();
        fedoraClient.close();
    }
}
