package cz.mzk.fofola.processes.donator_linker;

import cz.mzk.fofola.processes.utils.*;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Arrays;
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
        rootUuid = UuidUtils.checkAndMakeUuid(rootUuid);
        SolrQuery query = createQueryForRootUuid(rootUuid);
        Consumer<SolrDocument> donatorLinkingLogic = solrDoc -> {
            String docPID = (String) solrDoc.getFieldValue(SolrUtils.UUID_FIELD_NAME);
            logger.info(docPID);
            try {
                Document relsExt = fedoraClient.getRelsExt(docPID);
                Node descriptionRootNode = XMLUtils.getDescRootNode(relsExt);
                if (XMLUtils.getHasDonatorNode(donator, descriptionRootNode) == null) {
                    XMLUtils.insertHasDonatorNode(donator, relsExt, descriptionRootNode);
                    fedoraClient.setRelsExt(docPID, relsExt);
                }
            } catch (Exception e) {
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
            String docPID = (String) solrDoc.getFieldValue(SolrUtils.UUID_FIELD_NAME);
            try {
                Document relsExt = fedoraClient.getRelsExt(docPID);
                Node descriptionRootNode = XMLUtils.getDescRootNode(relsExt);
                Node hasDonatorNode = XMLUtils.getHasDonatorNode(donator, descriptionRootNode);
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
        String allDocsQueryStr = SolrUtils.wrapQueryStr(SolrUtils.ROOT_PID_FIELD_NAME, rootUuid.trim());
        SolrQuery query = new SolrQuery(allDocsQueryStr);
        query.addField(SolrUtils.UUID_FIELD_NAME);
        return query;
    }

    public void commitAndClose() throws IOException, SolrServerException {
        solrClient.commit();
        solrClient.close();
    }
}
