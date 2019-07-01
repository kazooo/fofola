package cz.mzk.integrity.solr_integrity;

import cz.mzk.integrity.model.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.stereotype.Component;

@Component
public class SolrIntegrityCheckerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SolrIntegrityCheckerThread.class);

    private String model;
    private long docCount;
    private int docsPerQuery;
    private String collectionName;
    private final SolrTemplate solrTemplate;

    public SolrIntegrityCheckerThread(SolrTemplate solrTemplate) {
        this.solrTemplate = solrTemplate;
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

        Cursor<SolrDocument> docs;

        long done = 0;
        while (done < docCount) {
            docs = solrTemplate.queryForCursor(collectionName, query, SolrDocument.class);
            while (docs.hasNext()) {
                SolrDocument doc = docs.next();
                logger.info(doc.getUuid());
                done++;
            }
        }

        logger.info("done solr integrity checking...");
    }
}
