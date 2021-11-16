package cz.mzk.fofola.process.dnnt;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import cz.mzk.fofola.model.doc.SolrDocument;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DnntLabelUnlinkRunnerImpl extends DnntLabelLinker implements DnntLabelLinkingRunner {

    public DnntLabelUnlinkRunnerImpl(final DnntLinkerParams linkerParams)
            throws XPathExpressionException {
        super(linkerParams);
    }

    @Override
    boolean processFoxml(String uuid, Document doc) {
        return removeDnntFlagFromFedora(uuid, doc);
    }

    @Override
    boolean processSolr(String uuid, SolrDocument doc) {
        return removeDnntFlagFromSolr(uuid, doc);
    }

    private boolean removeDnntFlagFromFedora(String uuid, Document doc) {
        boolean updated = false;
        try {
            final Node isDnntNode = getXmlDnntFlagNode(doc);
            final NodeList labelNodes = getXmlDnntLabelNodes(doc);
            if (isDnntNode != null) {
                removeDnntNode(isDnntNode);
                updated = true;
            }
            if (containsLabelNode(labelNodes, label)) {
                removeDnntNodes(labelNodes, label);
                updated = true;
            }
            if (updated) {
                fedoraApi.setRelsExt(uuid, doc);
            }
        } catch (XPathExpressionException | IOException | TransformerException e) {
            logger.severe("Can't remove FOXML DNNT label from " + uuid);
            logger.severe(e.getMessage());
            updated = false;
        }
        return updated;
    }

    private void removeDnntNode(Node nodeToRemove) {
        nodeToRemove.getParentNode().removeChild(nodeToRemove);
    }

    private void removeDnntNodes(NodeList nodesToRemove, String label) {
        for (int i = 0; i < nodesToRemove.getLength(); i++) {
            Node node = nodesToRemove.item(i);
            if (label.equals(node.getTextContent())) {
                removeDnntNode(node);
            }
        }
    }

    private boolean removeDnntFlagFromSolr(String uuid, SolrDocument doc) {
        boolean updated = false;
        List<String> labels = doc.getDnntLabels();

        if (labels != null && labels.contains(label)) {
            labels.remove(label);
            final SolrInputDocument inputDoc = modifyDnntParams(uuid, null, labels);
            try {
                solrClient.add(inputDoc);
                updated = true;
            } catch (IOException | SolrServerException e) {
                logger.severe("Can't set Solr DNNT label for " + uuid);
                logger.severe(e.getMessage());
            }
        }
        return updated;
    }
}
