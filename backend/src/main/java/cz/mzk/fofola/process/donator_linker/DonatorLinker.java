package cz.mzk.fofola.process.donator_linker;

import cz.mzk.fofola.api.fedora.FedoraApi;
import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.constants.solr.SolrField;
import cz.mzk.fofola.service.SolrService;
import cz.mzk.fofola.service.UuidService;
import cz.mzk.fofola.service.XMLService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DonatorLinker {

    private final SolrClient solrClient;
    private final FedoraApi fedoraApi;
    private final int maxDocsPerQuery;
    private final Logger logger;
    private final XMLService xmlService;

    public DonatorLinker(final FofolaConfiguration configuration, int maxDocsPerQuery, Logger logger)
            throws ParserConfigurationException, TransformerConfigurationException, XPathExpressionException {
        solrClient = SolrService.buildClient(configuration.getSolrHost());
        fedoraApi = ApiConfiguration.getFedoraApi(configuration);
        xmlService = new XMLService();
        this.logger = logger;
        this.maxDocsPerQuery = maxDocsPerQuery;
    }

    public void link(String rootUuid, String donator) throws IOException, SolrServerException {
        rootUuid = UuidService.makeUuid(rootUuid);
        SolrQuery query = createQueryForRootUuid(rootUuid);
        Consumer<SolrDocument> donatorLinkingLogic = solrDoc -> {
            String docPID = (String) solrDoc.getFieldValue(SolrField.UUID);
            logger.info(docPID);
            try {
                Document relsExt = fedoraApi.getRelsExt(docPID);
                if (xmlService.getHasDonatorNodes(donator, relsExt).size() == 0) {
                    xmlService.insertHasDonatorNode(donator, relsExt);
                    fedoraApi.setRelsExt(docPID, relsExt);
                }
            } catch (Exception e) {
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        };
        SolrService.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                query, solrClient, donatorLinkingLogic, maxDocsPerQuery
        );
    }

    public void unlink(String rootUuid, String donator) throws IOException, SolrServerException {
        rootUuid = UuidService.makeUuid(rootUuid);
        SolrQuery query = createQueryForRootUuid(rootUuid);
        Consumer<SolrDocument> donatorUnlinkingLogic = solrDoc -> {
            String docPID = (String) solrDoc.getFieldValue(SolrField.UUID);
            logger.info(docPID);
            try {
                Document relsExt = fedoraApi.getRelsExt(docPID);
                List<Node> donatorNodes = xmlService.getHasDonatorNodes(donator, relsExt);
                if (donatorNodes.size() != 0) {
                    donatorNodes.forEach(node -> node.getParentNode().removeChild(node));
                    fedoraApi.setRelsExt(docPID, relsExt);
                }
            } catch (IOException | TransformerException | XPathExpressionException e) {
                logger.severe(Arrays.toString(e.getStackTrace()));
            }
        };
        SolrService.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                query, solrClient, donatorUnlinkingLogic, maxDocsPerQuery
        );
    }

    private static SolrQuery createQueryForRootUuid(String rootUuid) {
        String allDocsQueryStr = SolrService.wrapQueryStr(SolrField.ROOT_PID, rootUuid.trim());
        SolrQuery query = new SolrQuery(allDocsQueryStr);
        query.addField(SolrField.UUID);
        return query;
    }

    public void commitAndClose() throws IOException, SolrServerException {
        solrClient.commit();
        solrClient.close();
    }
}
