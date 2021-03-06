package cz.mzk.fofola.service;

import cz.mzk.fofola.model.doc.FedoraDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class XMLService {

    private final XPathExpression uuidXPath;
    private final XPathExpression accessibilityXPath;
    private final XPathExpression imageUrlXPath;
    private final XPathExpression titleXPath;
    private final XPathExpression modelXPath;
    private final XPathExpression createdDateXPath;
    private final XPathExpression modifiedDateXPath;
    private final XPathExpression childrenXPath;
    private final XPathExpression donatorXPath;
    private final XPathExpression collectionsXPath;

    public XMLService() throws XPathExpressionException {
        modelXPath = compile(xpathForDcElementText("type"));
        titleXPath = compile(xpathForDcElementText("title"));
        uuidXPath = compile(xpathForRelsExtElementText("itemID"));
        donatorXPath = compile("/*/*/*[local-name() = 'hasDonator']"); // relative to RELS-EXT datastream
        imageUrlXPath = compile(xpathForRelsExtElementText("tiles-url"));
        accessibilityXPath = compile(xpathForRelsExtElementText("policy"));
        createdDateXPath = compile(xPathForProperty("info:fedora/fedora-system:def/model#createdDate"));
        modifiedDateXPath = compile(xPathForProperty("info:fedora/fedora-system:def/view#lastModifiedDate"));
        childrenXPath = compile(xpathForDs("RELS-EXT") + "/*/*" +
                "/*[starts-with(local-name(), 'has')]" +
                "[not(local-name() = 'hasModel')]" +
                "[not(local-name() = 'hasDonator')]/@*");
        collectionsXPath = compile(xpathForDs("RELS-EXT") + "/*/*/*[local-name() = 'isMemberOfCollection']/@rdf:resource");
    }

    public FedoraDocument parseFedoraDocument(Document doc) {
        if (doc == null) return null;
        try {
            doc.getDocumentElement().normalize();
            String uuid = uuidXPath.evaluate(doc);
            String title = titleXPath.evaluate(doc);
            String model = modelXPath.evaluate(doc);
            String imageUrl = imageUrlXPath.evaluate(doc);
            String accessibility = accessibilityXPath.evaluate(doc);
            String lastModDateStr = modifiedDateXPath.evaluate(doc);

            model = model.replace("model:", "");
            accessibility = accessibility.replace("policy:", "");
            imageUrl = imageUrl.isEmpty() ? "no image" : imageUrl + "/big.jpg";

            FedoraDocument fedoraDoc = new FedoraDocument(uuid);
            fedoraDoc.setTitle(title);
            fedoraDoc.setModel(model);
            fedoraDoc.setAccesibility(accessibility);
            fedoraDoc.setImageUrl(imageUrl);
            fedoraDoc.setModifiedDateStr(lastModDateStr);

            NodeList childAttrs = (NodeList) childrenXPath.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < childAttrs.getLength(); i++) {
                Attr attr = (Attr) childAttrs.item(i);
                String childUuid = attr.getValue().replace("info:fedora/", "");
                fedoraDoc.addChild(childUuid);
            }

            return fedoraDoc;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertHasDonatorNode(String donator, Document relsExt) {
        Element hasDonatorElement = relsExt.createElement("hasDonator");
        hasDonatorElement.setAttribute("xmlns", "http://www.nsdl.org/ontologies/relationships#");
        hasDonatorElement.setAttribute("rdf:resource", "info:fedora/donator:" + donator);
        Node descriptionRootNode = getDescRootNode(relsExt);
        descriptionRootNode.appendChild(hasDonatorElement);
    }

    public List<Node> getHasDonatorNodes(String donator, Document relsExt) throws XPathExpressionException {
        NodeList hasDonatorNodes = (NodeList) donatorXPath.evaluate(relsExt, XPathConstants.NODESET);
        List<Node> resultNodes = new ArrayList<>();
        for (int i = 0; i < hasDonatorNodes.getLength(); i++) {
            Node node = hasDonatorNodes.item(i);
            Attr donatorAttr = getAttributeWithName(node, "rdf:resource");
            if (donatorAttr != null && donatorAttr.getValue().contains(donator)) {
                resultNodes.add(node);
            }
        }
        return resultNodes;
    }

    public static XPathExpression compile(String expression) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath.compile(expression);
    }

    public String xpathForDcElementText(String elementName) {
        return xpathForDc() + xpathForElement(elementName) + xpathForElementText();
    }

    public String xpathForRelsExtElementText(String elementName) {
        return xpathForRelsExtElement(elementName) + xpathForElementText();
    }

    public String xpathForRelsExtElement(String elementName) {
        return xpathForRelsExt() + xpathForElement(elementName);
    }

    public String xpathForDc() {
        return xpathForDs("DC") + "/*";
    }

    public String xpathForRelsExt() {
        return xpathForDs("RELS-EXT") + "/*/*";
    }

    public String xpathForElement(String elName) {
        return "/*[local-name() = '" + elName + "']";
    }

    public String xpathForElementText() {
        return "/text()";
    }

    public String xpathForDs(String ds) {
        return "//*/*[local-name() = 'datastream'][@ID='" + ds + "']/*[local-name() = 'datastreamVersion'][last()]/*";
    }

    public String xPathForProperty(String propertyName) {
        return "//*/*[local-name() = 'objectProperties']/*[local-name() = 'property'][@NAME=\"" + propertyName + "\"]/@VALUE";
    }

    public Node getDescRootNode(Document relsExt) {
        return getFirstNode(relsExt.getDocumentElement(), "rdf:Description");
    }

    public Node getFirstNode(Element doc, String nodeName) {
        NodeList nodeList = doc.getElementsByTagName(nodeName);
        return nodeList.getLength() > 0 ? nodeList.item(0) : null;
    }

    private Attr getAttributeWithName(Node node, String attrName) {
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
