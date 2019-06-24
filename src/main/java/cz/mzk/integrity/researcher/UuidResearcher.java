package cz.mzk.integrity.researcher;

import cz.mzk.integrity.model.SolrDocument;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.logging.Logger;


@Component
public class UuidResearcher {

    private final SolrCommunicator solrCommunicator;
    private static final Logger logger = Logger.getLogger(UuidResearcher.class.getName());


    public UuidResearcher(SolrCommunicator solrCommunicator) {
        this.solrCommunicator = solrCommunicator;
    }

    public boolean isIndexed(String uuid) {
        SolrDocument solrDoc;
        try {
            solrDoc = solrCommunicator.getSolrDocByUuid(uuid);
        } catch (NoSuchElementException e) {
            logger.info(e.getMessage());
            solrDoc = null;
        }
        return solrDoc != null;
    }
}
