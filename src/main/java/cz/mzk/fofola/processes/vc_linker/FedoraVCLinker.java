package cz.mzk.fofola.processes.vc_linker;

import cz.mzk.fofola.processes.utils.FedoraClient;
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

    private final FedoraClient fedoraClient;
    private final String rdfNs = "rdf";

    public FedoraVCLinker(String fedoraHost, String fedoraUser, String fedoraPswd)
            throws ParserConfigurationException, TransformerConfigurationException {
        fedoraClient = new FedoraClient(fedoraHost, fedoraUser, fedoraPswd);
    }

    public boolean writeVCFor(String uuid, String vcId) throws TransformerException, IOException, SAXException {
        boolean canBeAdded = false;
        Document relsExt = fedoraClient.getRelsExt(uuid);
        if (relsExt == null) return canBeAdded;
        Element descriptionElement = (Element) relsExt.getElementsByTagNameNS(rdfNs, "Description").item(0);
        NodeList collectionNodes = descriptionElement.getElementsByTagNameNS(rdfNs, "isMemberOfCollection");
        String vcIdElementAttrName = "info:fedora/" + vcId;
        canBeAdded = collectionNodes == null || !containsVC(collectionNodes, vcIdElementAttrName);
        if (canBeAdded) {
            Element childElement = relsExt.createElementNS(rdfNs, "isMemberOfCollection");
            childElement.setAttributeNS(rdfNs, "resource", vcIdElementAttrName);
            descriptionElement.appendChild(childElement);
            fedoraClient.setRelsExt(uuid, relsExt);
        }
        return canBeAdded;
    }

    private boolean containsVC(NodeList collectionNodes, String vcIdElementAttrName) {
        for (int i = 0; i < collectionNodes.getLength(); i++) {
            Node collectionNode = collectionNodes.item(i);
            NamedNodeMap attributes = collectionNode.getAttributes();
            if (attributes.getLength() < 1) {
                continue;
            }
            Node attribute = attributes.getNamedItemNS(rdfNs, "resource");
            if (attribute.getTextContent().equals(vcIdElementAttrName)) {
                return true;
            }
        }
        return false;
    }

    public void close() {
        fedoraClient.close();
    }
}

