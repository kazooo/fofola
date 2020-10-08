package cz.mzk.fofola.service;

import cz.mzk.fofola.model.SolrDocument;
import cz.mzk.fofola.processes.utils.SolrUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SolrCommunicator {

    private final SolrClient solrClient;

    public SolrCommunicator(@Value("${SOLR_HOST}") String solrHost) {
        solrClient = SolrUtils.buildClient(solrHost);
    }

    public SolrDocument getSolrDocByUuid(String uuid) {
        try {
            return SolrDocument.convert(solrClient.getById(uuid));
        } catch (SolrServerException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<SolrDocument> getSolrDocsByRootPid(String rootUuid) {
        try {
            String queryStr = SolrDocument.ROOT_PID + ":\"" + rootUuid + "\"";
            SolrQuery params = new SolrQuery(queryStr);
            return SolrDocument.convert(solrClient.query(params).getResults());
        } catch (SolrServerException | IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
