package cz.mzk.fofola.processes.utils;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.concurrent.atomic.AtomicReference;

public class XMLUtils {

    public static void insertHasDonatorNode(String donator, Document relsExt, Node descriptionRootNode) {
        Element hasDonatorElement = relsExt.createElement("hasDonator");
        hasDonatorElement.setAttribute("xmlns", "http://www.nsdl.org/ontologies/relationships#");
        hasDonatorElement.setAttribute("rdf:resource", "info:fedora/donator:" + donator);
        descriptionRootNode.appendChild(hasDonatorElement);
    }

    public static Node getHasDonatorNode(String donator, Node descriptionRootNode) {
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

    public static Node getDescRootNode(Document relsExt) {
        return FedoraUtils.getFirstNode(
                relsExt.getDocumentElement(), "rdf:Description"
        );
    }
}
