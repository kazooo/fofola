package cz.mzk.fofola.process.dnnt;

import cz.mzk.fofola.constants.SolrFieldName;
import cz.mzk.fofola.model.doc.SolrDocument;
import org.apache.solr.client.solrj.SolrClient;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.service.SolrService;
import cz.mzk.fofola.enums.dnnt.DnntLabelEnum;

public abstract class DnntLabelLinker {

    protected final String label;
    protected final SolrClient solrClient;
    protected final FedoraApi fedoraApi;
    protected final int maxDocsPerQuery;
    protected final Logger logger;
    protected final boolean processRecursive;

    protected final XPathExpression dnntXPathExpr;
    protected final XPathExpression dnntLabelXPathExpr;
    protected final XPathExpression dnntDocRoot;

    protected static final String FOXML_DNNT_LABEL = "dnnt-label";
    protected static final String FOXML_DNNT_FLAG = "dnnt";

    public DnntLabelLinker(final DnntLinkerParams linkerParams) throws XPathExpressionException {
        this.logger = linkerParams.getLogger();
        this.label = linkerParams.getLabel();
        this.maxDocsPerQuery = linkerParams.getMaxDocsPerQuery();
        this.processRecursive = linkerParams.isProcessRecursive();
        this.solrClient = linkerParams.getSolrClient();
        this.fedoraApi = linkerParams.getFedoraApi();

        final XPath xmlPath = XPathFactory.newInstance().newXPath();
        dnntXPathExpr = xmlPath.compile("//*[local-name() = 'dnnt']");
        dnntDocRoot = xmlPath.compile("//*[local-name() = 'Description']");
        dnntLabelXPathExpr = xmlPath.compile("//*[local-name() = 'dnnt-label']");
    }

    abstract boolean processFoxml(String uuid, Document doc);
    abstract boolean processSolr(String uuid, SolrDocument doc);

    public void run(final String uuid) throws Exception {
        try {
            updateInFedora(uuid);
            updateInSolr(uuid);
        } catch (SolrServerException | IOException e) {
            logger.severe("Can't process " + uuid);
            logger.severe(e.getMessage());
            throw new Exception(e);
        }
    }

    private void updateInFedora(final String uuid) {
        logger.info("Try to process DNNT nodes in FOXML of " + uuid + "...");
        final Document foxml = fedoraApi.getRelsExt(uuid);
        final boolean foxmlUpdated = processFoxml(uuid, foxml);
        if (foxmlUpdated) {
            logger.info("Successfully updated FOXML of " + uuid);
        } else {
            logger.info("FOXML of " + uuid + " doesn't require to process DNNT flags...");
        }
    }

    private void updateInSolr(final String uuid) throws SolrServerException, IOException {
        logger.info("Try to process DNNT flags in Solr documents of " + uuid + "...");
        final SolrQuery solrQuery = getSolrQuery(uuid);
        final AtomicInteger updatedDocs = new AtomicInteger(0);
        SolrService.iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
                solrQuery, solrClient, solrDoc -> {
                    final SolrDocument doc = SolrDocument.convert(solrDoc);
                    final boolean updated = processSolr(doc.getUuid(), doc);
                    if (updated) {
                        updatedDocs.incrementAndGet();
                    }
                }, 1_000
        );
        logger.info("Updated: " + updatedDocs.get() + " docs.");
    }

    private SolrQuery getSolrQuery(final String uuid) {
        if (!processRecursive) {
            return new SolrQuery("PID:\"" + uuid + "\"");
        } else {
            return new SolrQuery("pid_path:/.*\\/" + uuid + "\\/.*/");
        }
    }

    protected Node getRelsExtDocRoot(Document doc) throws XPathExpressionException {
        return (Node) dnntDocRoot.evaluate(doc, XPathConstants.NODE);
    }

    protected Node getXmlDnntFlagNode(Document doc) throws XPathExpressionException {
        return (Node) dnntXPathExpr.evaluate(doc, XPathConstants.NODE);
    }

    protected NodeList getXmlDnntLabelNodes(Document doc) throws XPathExpressionException {
        return (NodeList) dnntLabelXPathExpr.evaluate(doc, XPathConstants.NODESET);
    }

    protected boolean containsLabelNode(final NodeList nodeList, final String label) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (label.equals(node.getTextContent())) {
                return true;
            }
        }
        return false;
    }

    protected SolrInputDocument modifyDnntParams(String uuid, Boolean isDnnt, List<String> labels) {
        final String SET = "set";
        final SolrInputDocument inputDoc = new SolrInputDocument();
        inputDoc.addField(SolrFieldName.ID, uuid);
        inputDoc.addField(SolrFieldName.DNNT, Collections.singletonMap(SET, isDnnt));
        inputDoc.addField(SolrFieldName.DNNT_LABELS, Collections.singletonMap(SET, labels));
        inputDoc.addField(SolrFieldName.MODIFIED_DATE, Collections.singletonMap(SET, new Date()));
        return inputDoc;
    }

    public void commitAndClose() {
        try {
            solrClient.commit();
            solrClient.close();
        } catch (SolrServerException | IOException e) {
            logger.severe(e.getMessage());
        }
    }
}
