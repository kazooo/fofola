package cz.mzk.fofola.repository;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.model.doc.FedoraDocument;
import cz.mzk.fofola.service.XMLService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;


@Repository
@AllArgsConstructor
public class FedoraDocumentRepository {

    private final XMLService xmlService;
    private final FedoraApi fedoraApi;

    public FedoraDocument getByUuid(String uuid) {
        return xmlService.parseFedoraDocument(fedoraApi.getFullFoxmlByUuid(uuid));
    }

    public void makePrivate(String uuid) {
        changeAccessibility(uuid, "private");
    }

    public void makePublic(String uuid) {
        changeAccessibility(uuid, "public");
    }

    private void changeAccessibility(String uuid, String accessibility) {
        try {
            Document relsExt = fedoraApi.getRelsExt(uuid);
            XPathExpression relsExtPolicyXPath = xmlService.compile(
                    "//*" + xmlService.xpathForElement("policy")
            );
            Node relsExtPolicy = (Node) relsExtPolicyXPath.evaluate(relsExt, XPathConstants.NODE);
            relsExtPolicy.setTextContent("policy:" + accessibility);
            fedoraApi.setRelsExt(uuid, relsExt);

            Document dc = fedoraApi.getDc(uuid);
            XPathExpression dcPolicyXPath = xmlService.compile(
                    "//*" + xmlService.xpathForElement("dc:rights")
            );
            Node dcPolicy = (Node) relsExtPolicyXPath.evaluate(dcPolicyXPath, XPathConstants.NODE);
            dcPolicy.setTextContent("policy:" + accessibility);
            fedoraApi.setDc(uuid, dc);
        } catch (TransformerException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
