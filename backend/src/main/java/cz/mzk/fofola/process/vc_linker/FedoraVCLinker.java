package cz.mzk.fofola.process.vc_linker;

import cz.mzk.fofola.api.fedora.FedoraApi;
import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.service.XMLService;
import org.w3c.dom.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.logging.Logger;


/**
 * This linker load RELS-EXT data stream for each uuid and creates link to virtual collection
 * by adding XML element with virtual collection ID to mentioned data stream.
 * At the end it pushes new data stream to Fedora. New triplet for virtual collection link
 * is created automatically by Fedora.
 *
 * @author Aleksei Ermak
 */
public class FedoraVCLinker {

    private final FedoraApi fedoraApi;
    private final Logger logger;
    private final XPathExpression collectionsXPath;
    private final XPathExpression childrenXPath;

    public FedoraVCLinker(final FofolaConfiguration configuration, final Logger log)
            throws ParserConfigurationException, TransformerConfigurationException, XPathExpressionException {
        logger = log;
        fedoraApi = ApiConfiguration.getFedoraApi(configuration);
        collectionsXPath = XMLService.compile("/*/*/*[local-name() = 'isMemberOfCollection']/@*[local-name() = 'resource']");
        childrenXPath = XMLService.compile("/*/*" +
                "/*[starts-with(local-name(), 'has')]" +
                "[not(local-name() = 'hasModel')]" +
                "[not(local-name() = 'hasDonator')]/@*");
    }

    public void addVcRecursively(String vcId, String uuid)
            throws XPathExpressionException, IOException, TransformerException {
        Document relsExt = fedoraApi.getRelsExt(uuid);
        boolean hasBeenUpdated = addVc(relsExt, uuid, vcId);
        if (hasBeenUpdated) {  // parent hadn't collection -> update children
            NodeList childAttrs = (NodeList) childrenXPath.evaluate(relsExt, XPathConstants.NODESET);
            for (int i = 0; i < childAttrs.getLength(); i++) {
                Attr attr = (Attr) childAttrs.item(i);
                String childUuid = attr.getValue().replace("info:fedora/", "");
                addVcRecursively(vcId, childUuid);
            }
        }
    }

    public boolean addVc(Document relsExt, String uuid, String vcId)
            throws XPathExpressionException, IOException, TransformerException {
        if (relsExt == null) return false;

        boolean hasCollection = false;
        String vcIdAttrName = "info:fedora/" + vcId;
        NodeList collectionNodes = (NodeList) collectionsXPath.evaluate(relsExt, XPathConstants.NODESET);
        for (int i = 0; i < collectionNodes.getLength(); i++) {
            Attr collectionNode = (Attr) collectionNodes.item(i);
            if (collectionNode.getValue().equals(vcIdAttrName)) {
                hasCollection = true;
            }
        }
        if (!hasCollection) {
            logger.info("Update: " + uuid);
            Element description = getDescription(relsExt);
            Element childElement = relsExt.createElement("rdf:isMemberOfCollection");
            childElement.setAttribute("rdf:resource", vcIdAttrName);
            description.appendChild(childElement);
            fedoraApi.setRelsExt(uuid, relsExt);
        }
        return !hasCollection;
    }

    public void removeVcRecursively(String vcId, String uuid)
            throws TransformerException, XPathExpressionException, IOException {
        Document relsExt = fedoraApi.getRelsExt(uuid);
        boolean hasBeenUpdated = removeVc(relsExt, uuid, vcId);
        if (hasBeenUpdated) {  // parent contained vc id -> check and remove from children
            NodeList childAttrs = (NodeList) childrenXPath.evaluate(relsExt, XPathConstants.NODESET);
            for (int i = 0; i < childAttrs.getLength(); i++) {
                Attr attr = (Attr) childAttrs.item(i);
                String childUuid = attr.getValue().replace("info:fedora/", "");
                removeVcRecursively(vcId, childUuid);
            }
        }
    }

    public boolean removeVc(Document relsExt, String uuid, String vcId)
            throws XPathExpressionException, IOException, TransformerException {
        if (relsExt == null) return false;

        boolean hasBeenUpdated = false;
        NodeList collectionAttrs = (NodeList) collectionsXPath.evaluate(relsExt, XPathConstants.NODESET);
        for (int i = 0; i < collectionAttrs.getLength(); i++) {
            Attr collectionAttr = (Attr) collectionAttrs.item(i);
            if (collectionAttr.getValue().contains(vcId)) {
                Node collectionNode = collectionAttr.getOwnerElement();
                collectionNode.getParentNode().removeChild(collectionNode);
                hasBeenUpdated = true;
            }
        }
        if (hasBeenUpdated) {
            logger.info("Update: " + uuid);
            fedoraApi.setRelsExt(uuid, relsExt);
        }
        return hasBeenUpdated;
    }

    private Element getDescription(Document relsExt) {
        return (Element) relsExt.getElementsByTagName("rdf:Description").item(0);
    }
}

