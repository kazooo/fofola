package cz.mzk.fofola.repository;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.SolrDocument;
import cz.mzk.fofola.process.utils.SolrUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;


@Repository
public class SolrDocumentRepository {

    private final SolrClient solrClient;

    public SolrDocumentRepository(FofolaConfiguration config) {
        solrClient = SolrUtils.buildClient(config.getSolrHost());
    }

    public SolrDocument getByUuid(String uuid) {
        try {
            return SolrDocument.convert(solrClient.getById(uuid));
        } catch (SolrServerException | IOException e) {
            return null;
        }
    }

    public List<SolrDocument> getChildByParentUuid(String parentUuid) {
        try {
            String queryStr = SolrDocument.PARENT_PID + ":\"" + parentUuid + "\" AND !PID:\"" + parentUuid + "\"";
            SolrQuery params = new SolrQuery(queryStr);
            return SolrDocument.convert(solrClient.query(params).getResults());
        } catch (SolrServerException | IOException e) {
            return Collections.emptyList();
        }
    }
}
