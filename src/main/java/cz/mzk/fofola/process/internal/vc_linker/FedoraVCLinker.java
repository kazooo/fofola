package cz.mzk.fofola.process.internal.vc_linker;

import cz.mzk.fofola.api.FedoraApi;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.io.IOException;

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

    public FedoraVCLinker(String fedoraHost, String fedoraUser, String fedoraPswd)
            throws ParserConfigurationException, TransformerConfigurationException {
        fedoraApi = new FedoraApi(fedoraHost, fedoraUser, fedoraPswd);
    }

    public boolean writeVCFor(String uuid, String vcId)
            throws TransformerException, IOException, SAXException {
        Document relsExt = fedoraApi.getRelsExt(uuid);
        if (relsExt == null) return false;

        Element description = getDescription(relsExt);
        NodeList collections = getCollections(description);
        String vcIdAttrName = "info:fedora/" + vcId;

        if (getCollectionWithAttrName(collections, vcIdAttrName) == null) {
            Element childElement = relsExt.createElement("rdf:isMemberOfCollection");
            childElement.setAttribute("rdf:resource", vcIdAttrName);
            description.appendChild(childElement);
            fedoraApi.setRelsExt(uuid, relsExt);
        }
        return true; // vc id is already in RELS-EXT or was created now
    }

    public boolean removeVCFor(String uuid, String vcId)
            throws IOException, SAXException, TransformerException {
        Document relsExt = fedoraApi.getRelsExt(uuid);
        if (relsExt == null) return false;

        NodeList collections = getCollections(getDescription(relsExt));
        String vcIdAttrName = "info:fedora/" + vcId;

        Node collection = getCollectionWithAttrName(collections, vcIdAttrName);
        boolean canBeRemoved = collection != null;
        if (canBeRemoved) {
            collection.getParentNode().removeChild(collection);
            fedoraApi.setRelsExt(uuid, relsExt);
        }
        return canBeRemoved;
    }

    private Node getCollectionWithAttrName(NodeList collections, String vcIdAttrName) {
        if (collections == null) return null;
        for (int i = 0; i < collections.getLength(); i++) {
            Node collection = collections.item(i);
            NamedNodeMap attributes = collection.getAttributes();
            if (attributes.getLength() < 1) continue;

            Node attr = attributes.getNamedItem("resource");
            if (attr == null) attr = attributes.getNamedItem("rdf:resource");
            String attributeName = attr.getTextContent();
            if (attributeName.equals(vcIdAttrName)) return collection;
        }
        return null;
    }

    private Element getDescription(Document relsExt) {
        return (Element) relsExt.getElementsByTagName("rdf:Description").item(0);
    }

    private NodeList getCollections(Element description) {
        return description.getElementsByTagName("rdf:isMemberOfCollection");
    }
}

