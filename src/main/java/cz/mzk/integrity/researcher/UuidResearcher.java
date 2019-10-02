package cz.mzk.integrity.researcher;

import cz.mzk.integrity.model.FedoraDocument;
import cz.mzk.integrity.model.KrameriusDocument;
import cz.mzk.integrity.model.SolrDocument;
import cz.mzk.integrity.service.FedoraCommunicator;
import cz.mzk.integrity.service.SolrCommunicator;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.logging.Logger;


@Component
public class UuidResearcher {

    private final SolrCommunicator solrCommunicator;
    private final FedoraCommunicator fedoraCommunicator;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final Logger logger = Logger.getLogger(UuidResearcher.class.getName());


    public UuidResearcher(SolrCommunicator solrCommunicator, FedoraCommunicator fedoraCommunicator) {
        this.solrCommunicator = solrCommunicator;
        this.fedoraCommunicator = fedoraCommunicator;
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
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
        return fedoraCommunicator.getFedoraDocByUuid(uuid);
    }

    public KrameriusDocument fillKrameriusDoc(KrameriusDocument doc) {
        String uuid = doc.getUuid();
        SolrDocument solrDoc = getSolrDoc(uuid);
        FedoraDocument fedoraDoc = getFedoraDoc(uuid);

        doc.setIndexed(solrDoc != null);
        doc.setStored(fedoraDoc != null);

        if (solrDoc != null) {
            if (solrDoc.getModifiedDate() != null) {
                doc.setSolrModifiedDate(sdf.format(solrDoc.getModifiedDate()));
            }
            doc.setAccessibilityInSolr(solrDoc.getAccessibility());
            doc.setRootTitle(solrDoc.getRootTitle());
        }

        if (fedoraDoc != null) {
            doc.setAccessibilityInFedora(fedoraDoc.getAccesibility());
            doc.setModel(fedoraDoc.getModel());
            doc.setFedoraModifiedDate(fedoraDoc.getModifiedDateStr());
            doc.setImgUrl(fedoraDoc.getImageUrl());
        }

        return doc;
    }
}
