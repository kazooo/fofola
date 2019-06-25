package cz.mzk.integrity.researcher;

import cz.mzk.integrity.model.FedoraDocument;
import cz.mzk.integrity.model.KrameriusDocument;
import cz.mzk.integrity.model.SolrDocument;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;


@Component
public class UuidResearcher {

    private final SolrCommunicator solrCommunicator;
    private final FedoraCommunicator fedoraCommunicator;
    private static final Logger logger = Logger.getLogger(UuidResearcher.class.getName());


    public UuidResearcher(SolrCommunicator solrCommunicator, FedoraCommunicator fedoraCommunicator) {
        this.solrCommunicator = solrCommunicator;
        this.fedoraCommunicator = fedoraCommunicator;
    }

    private SolrDocument getSolrDoc(String uuid) {
        SolrDocument solrDoc;
        try {
            solrDoc = solrCommunicator.getSolrDocByUuid(uuid);
        } catch (NoSuchElementException e) {
            logger.info(e.getMessage());
            solrDoc = null;
        }
        return solrDoc;
    }

    private FedoraDocument getFedoraDoc(String uuid) {
        FedoraDocument fedoraDoc;
        try {
            fedoraDoc = fedoraCommunicator.getFedoraDocByUuid(uuid);
        } catch (SAXException | ParserConfigurationException | IOException | NoSuchElementException e) {
            logger.info(e.getMessage());
            fedoraDoc = null;
        }
        return fedoraDoc;
    }

    public KrameriusDocument fillKrameriusDoc(KrameriusDocument doc) {
        String uuid = doc.getUuid();
        SolrDocument solrDoc = getSolrDoc(uuid);
        FedoraDocument fedoraDoc = getFedoraDoc(uuid);

        doc.setIndexed(solrDoc != null);
        doc.setStored(fedoraDoc != null);

        if (solrDoc != null) {
            doc.setAccessibilityInSolr(solrDoc.getAccessibility());
        }

        if (fedoraDoc != null) {
            doc.setAccessibilityInFedora(fedoraDoc.getAccesibility());
            doc.setModel(fedoraDoc.getModel());
        }

        return doc;
    }
}
