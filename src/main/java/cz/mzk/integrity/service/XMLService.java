package cz.mzk.integrity.service;

import cz.mzk.integrity.model.FedoraDocument;
import cz.mzk.integrity.model.UuidProblem;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;


@Service
public class XMLService {

    private static Transformer transformer;
    private static DocumentBuilder docBuilder;

    private List<String> uuidElementNames = Arrays.asList("dc:identifier");
    private List<String> accessibilityElementNames = Arrays.asList("dc:rights");
    private List<String> modelElementNames = Arrays.asList("dc:type");
    private List<String> imageUrlElementNames = Arrays.asList("tiles-url", "kramerius4:tiles-url");
    private List<String> rdfDescElementNames = Arrays.asList("rdf:Description");
    private List<String> lastModDateElementNames = Arrays.asList("audit:date");

    static {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Document newDoc() {
        return docBuilder.newDocument();
    }

    public Element createElement(Document doc, Element parentElement, String elementName) {
        Element newElement = doc.createElement(elementName);
        parentElement.appendChild(newElement);
        return newElement;
    }

    public void setTextNode(Document doc, Element element, String text) {
        element.appendChild(doc.createTextNode(text));
    }

    public void saveDoc(Document doc, String outFileName) throws TransformerException {
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(outFileName));
        transformer.transform(source, result);
    }

    public FedoraDocument parseFedoraDocument(Document doc) {
        doc.getDocumentElement().normalize();

        String uuid = extractUuidFromRelsExt(doc);

        // get the last element in list, because the last element is the latest accessibility
        String accessibility = getLastElementTextContent(
                getElementsByNameFromList(doc, accessibilityElementNames), UuidProblem.NO_ACCESS);
        accessibility = accessibility.substring(accessibility.indexOf(":")+1);  // policy:private

        String model = getLastElementTextContent(
                getElementsByNameFromList(doc, modelElementNames),
                UuidProblem.NO_MODEL
        );
        model = model.substring(model.indexOf(":")+1);  // model:periodical

        String imageUrl = getLastElementTextContent(
                getElementsByNameFromList(doc, imageUrlElementNames),
                UuidProblem.NO_IMAGE
        );

        String lastModDateStr = getLastElementTextContent(
                getElementsByNameFromList(doc, lastModDateElementNames),
                UuidProblem.NO_MODIF_DATE
        );

        FedoraDocument fedoraDoc = new FedoraDocument(uuid);
        fedoraDoc.setAccesibility(accessibility);
        fedoraDoc.setModel(model);
        fedoraDoc.setImageUrl(imageUrl.equals(UuidProblem.NO_IMAGE) ? imageUrl : imageUrl + "/big.jpg");
        fedoraDoc.setModifiedDateStr(lastModDateStr);
        extractChildsFromDoc(doc, fedoraDoc);

        return fedoraDoc;
    }

    private String extractUuidFromRelsExt(Document doc) {
        NodeList rdfDesc = getElementsByNameFromList(doc, rdfDescElementNames);
        Node rdfDescNode = rdfDesc.item(rdfDesc.getLength()-1);
        String uuid = getStringAttrValue((Element)rdfDescNode, "rdf:about");
        uuid = uuid.replace("info:fedora/", "");
        return uuid;
    }

    private void extractChildsFromDoc(Document doc, FedoraDocument fedoraDoc) {
        NodeList rdfDesc = getElementsByNameFromList(doc, rdfDescElementNames);
        if (rdfDesc == null || rdfDesc.getLength() < 1) {
            fedoraDoc.addChild(UuidProblem.NO_CHILD);
            return;
        }
        Node rdfDescNode = rdfDesc.item(rdfDesc.getLength()-1);
        NodeList nodeList = rdfDescNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                String tagName = currentNode.getNodeName();
                if (tagName.contains("has") &&
                        hasAttribute((Element) currentNode, "http://www.nsdl.org/ontologies/relationships#")
                    || tagName.contains("kramerius:has")) {
                    String childUuid = getStringAttrValue((Element)currentNode, "rdf:resource");
                    childUuid = childUuid.replace("info:fedora/", "");
                    fedoraDoc.addChild(childUuid);
                }
            }
        }
    }

    private static boolean hasAttribute(Element element, String value) {
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node node = attributes.item(i);
            if (value.equals(node.getNodeValue())) {
                return true;
            }
        }
        return false;
    }

    private static String getStringAttrValue(Element element, String attrName) {
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node node = attributes.item(i);
            if (attrName.equals(node.getNodeName())) {
                return node.getTextContent();
            }
        }
        return "";
    }

    private NodeList getElementsByNameFromList(Document doc, List<String> elementNames) {
        NodeList nodes = null;

        for (String elementName : elementNames) {
            nodes = doc.getElementsByTagName(elementName);
            if (nodes != null && nodes.getLength() > 0) { break; }
        }

        return nodes;
    }

    private String getElementTextContent(NodeList nodeList, int i) {
        return nodeList.item(i).getTextContent();
    }

    private String getLastElementTextContent(NodeList nodeList, String problem) {
        if (nodeList == null || nodeList.getLength() < 1) {
            return problem;
        }
        return nodeList.item(nodeList.getLength()-1).getTextContent();
    }
}
