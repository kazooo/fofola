package cz.mzk.fofola.process.dnnt;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.mzk.fofola.model.doc.SolrDocument;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DnntLabelLinkRunnerImpl extends DnntLabelLinker implements DnntLabelLinkingRunner {

    public DnntLabelLinkRunnerImpl(final DnntLinkerParams linkerParams)
            throws XPathExpressionException {
        super(linkerParams);
    }

    @Override
    boolean processFoxml(String uuid, Document doc) {
        return addDnntFlagToFedora(uuid, doc);
    }

    @Override
    boolean processSolr(String uuid, SolrDocument doc) {
        return addDnntFlagToSolr(uuid, doc);
    }

    private boolean addDnntFlagToFedora(String uuid, Document doc) {
        boolean updated = false;
        try {
            final Node relsExtDocRoot = getRelsExtDocRoot(doc);
            final Node isDnntNode = getXmlDnntFlagNode(doc);
            final NodeList labelNodes = getXmlDnntLabelNodes(doc);
            if (isDnntNode == null) {
                addDnntNode(relsExtDocRoot, doc, FOXML_DNNT_FLAG, "true");
                updated = true;
            }
            if (!containsLabelNode(labelNodes, label)) {
                addDnntNode(relsExtDocRoot, doc, FOXML_DNNT_LABEL, label);
                updated = true;
            }
            if (updated) {
                fedoraApi.setRelsExt(uuid, doc);
            }
        } catch (XPathExpressionException | IOException | TransformerException e) {
            logger.severe("Can't set FOXML DNNT label for " + uuid);
            logger.severe(e.getMessage());
            updated = false;
        }
        return updated;
    }

    private void addDnntNode(Node relsExtRootNode, Document doc, String nodeName, String textContent) {
        Node dnnFlagNode = doc.createElement(nodeName);
        ((Element)dnnFlagNode).setAttribute("xmlns","http://www.nsdl.org/ontologies/relationships#");
        dnnFlagNode.setTextContent(textContent);
        relsExtRootNode.appendChild(dnnFlagNode);
    }

    private boolean addDnntFlagToSolr(final String uuid, final SolrDocument doc) {
        boolean updated = false;
        List<String> labels = doc.getDnntLabels();
        final Boolean isDnnt = doc.getDnnt();

        /* update only "dnnt" field even if the document contains required label */
        if (isDnnt == null || !isDnnt) {
            updated = true;
        }

        if (labels != null && !labels.contains(label)) {
            labels.add(label);
            updated = true;
        } else if (labels == null) {
            labels = new ArrayList<>() {{
                add(label);
            }};
            updated = true;
        }

        if (updated) {
            try {
                final SolrInputDocument inputDoc = modifyDnntParams(uuid, true, labels);
                solrClient.add(inputDoc);
            } catch (IOException | SolrServerException e) {
                logger.severe("Can't set Solr DNNT label for " + uuid);
                logger.severe(e.getMessage());
                updated = false;
            }
        }
        return updated;
    }
}
