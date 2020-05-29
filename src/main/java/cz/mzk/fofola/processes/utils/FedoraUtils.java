package cz.mzk.fofola.processes.utils;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.function.Consumer;

public class FedoraUtils {

    public static void makePrivate(String uuid, FedoraClient fedoraClient)
            throws IOException, SAXException, TransformerException {
        changeAccessibility(uuid, fedoraClient, "private");
    }

    public static void makePublic(String uuid, FedoraClient fedoraClient)
            throws IOException, SAXException, TransformerException {
        changeAccessibility(uuid, fedoraClient, "public");
    }

    private static void changeAccessibility(String uuid, FedoraClient fedoraClient, String accessibility)
            throws IOException, SAXException, TransformerException {
        Document relsExt = fedoraClient.getRelsExt(uuid);
        Node descriptionRootNode = getFirstNodeNS(relsExt.getDocumentElement(), "rdf", "Description");
        iterateChildNodes(descriptionRootNode, node -> {
            if (node.getNodeName().equals("policy")) {
                node.setTextContent("policy:"+accessibility);
            }
        });
        fedoraClient.setRelsExt(uuid, relsExt);

        Document dc = fedoraClient.getDc(uuid);
        iterateChildNodes(dc.getDocumentElement(), node -> {
            if (node.getNodeName().equals("dc:rights")) {
                node.setTextContent("policy:"+accessibility);
            }
        });
        fedoraClient.setDc(uuid, dc);
    }

    public static Node getFirstNodeNS(Element doc, String ns, String nodeName) {
        NodeList nodeList = doc.getElementsByTagNameNS(ns, nodeName);
        return nodeList.getLength() > 0 ? nodeList.item(0) : null;
    }

    public static void iterateChildNodes(Node root, Consumer<Node> nodeConsumer) {
        iterateNodes(root.getChildNodes(), nodeConsumer);
    }

    private static void iterateNodes(NodeList nodes, Consumer<Node> nodeConsumer) {
        int nodeListLength = nodes.getLength();
        for (int i = 0; i < nodeListLength; i++) {
            nodeConsumer.accept(nodes.item(i));
        }
    }

    public static Attr getAttributeWithName(Node node, String attrName) {
        if (node == null) return null;
        NamedNodeMap attributes = node.getAttributes();
        int numAttrs = attributes.getLength();
        for (int i = 0; i < numAttrs; i++) {
            Attr attr = (Attr) attributes.item(i);
            if (attrName.equals(attr.getName())) {
                return attr;
            }
        }
        return null;
    }
}
