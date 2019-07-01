package cz.mzk.integrity.solr_integrity;

import cz.mzk.integrity.model.SolrDocument;
import cz.mzk.integrity.service.SolrCommunicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SolrIntegrityCheckerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SolrIntegrityCheckerThread.class);

    private String model;
    private long docCount;
    private int docsPerQuery;
    private String collectionName;

    private final SolrCommunicator solrCommunicator;

    public SolrIntegrityCheckerThread(SolrCommunicator solrCommunicator) {
        this.solrCommunicator = solrCommunicator;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setDocCount(long docCount) {
        this.docCount = docCount;
    }

    public void setDocsPerQuery(int docsPerQuery) {
        this.docsPerQuery = docsPerQuery;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public void run() {
        logger.info("run solr integrity checking...");

        SimpleQuery query = new SimpleQuery(SolrDocument.MODEL + ":" + model)
                .addSort(Sort.by(SolrDocument.ID)).setRows(docsPerQuery);

        long done = 0;
        List<SolrDocument> docs;

        while (done < docCount) {
            docs = solrCommunicator.cursorQuery(collectionName, query);
            done += docs.size();
        }

        logger.info("done solr integrity checking...");
    }
}
