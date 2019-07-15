package cz.mzk.integrity.service;

import cz.mzk.integrity.model.FedoraDocument;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;


@Service
public class XMLService {

    private List<String> uuidElementNames = Arrays.asList("dc:identifier");
    private List<String> accessibilityElementNames = Arrays.asList("dc:rights");
    private List<String> modelElementNames = Arrays.asList("dc:type");


    public FedoraDocument parseFedoraDocument(Document doc) {
        doc.getDocumentElement().normalize();

        String uuid = getElementTextContent(getElementsByNameFromList(doc, uuidElementNames), 0);

        String accessibility = getElementTextContent(getElementsByNameFromList(doc, accessibilityElementNames), 0);
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
