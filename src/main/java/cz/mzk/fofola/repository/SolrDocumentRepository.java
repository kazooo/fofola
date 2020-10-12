package cz.mzk.fofola.repository;

import cz.mzk.fofola.model.SolrDocument;
import cz.mzk.fofola.processes.utils.SolrUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;


@Repository
public class SolrDocumentRepository {

    private final SolrClient solrClient;

    public SolrDocumentRepository(@Value("${SOLR_HOST}") String solrHost) {
        solrClient = SolrUtils.buildClient(solrHost);
    }

    public SolrDocument getByUuid(String uuid) {
        try {
            return SolrDocument.convert(solrClient.getById(uuid));
        } catch (SolrServerException | IOException e) {
            return null;
        }
    }

    public List<SolrDocument> getByRootPid(String rootUuid) {
        try {
            String queryStr = SolrDocument.ROOT_PID + ":\"" + rootUuid + "\"";
            SolrQuery params = new SolrQuery(queryStr);
            return SolrDocument.convert(solrClient.query(params).getResults());
        } catch (SolrServerException | IOException e) {
            return Collections.emptyList();
        }
    }
}
