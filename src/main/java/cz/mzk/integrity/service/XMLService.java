package cz.mzk.integrity.service;

import cz.mzk.integrity.model.FedoraDocument;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

        String uuid = getElementTextContent(getElementsByNameFromList(doc, uuidElementNames), 0);

        NodeList accessibilityNodes = getElementsByNameFromList(doc, accessibilityElementNames);
        // get the last element in list, because the last element is the latest accessibility
        String accessibility = getElementTextContent(accessibilityNodes, accessibilityNodes.getLength()-1);
        accessibility = accessibility.substring(accessibility.indexOf(":")+1);  // policy:private

        String model = getElementTextContent(getElementsByNameFromList(doc, modelElementNames), 0);
        model = model.substring(model.indexOf(":")+1);  // model:periodical

        FedoraDocument fedoraDoc = new FedoraDocument(uuid);
        fedoraDoc.setAccesibility(accessibility);
        fedoraDoc.setModel(model);

        return fedoraDoc;
    }

    private NodeList getElementsByNameFromList(Document doc, List<String> elementNames) {
        NodeList nodes = null;

        for (String elementName : elementNames) {
            nodes = doc.getElementsByTagName(elementName);
            if (nodes != null) { break; }
        }

        return nodes;
    }

    private String getElementTextContent(NodeList nodeList, int i) {
        return nodeList.item(i).getTextContent();
    }
}
