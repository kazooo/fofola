package cz.mzk.fofola.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CursorMarkParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SolrRecordTransporter {

    private HttpSolrClient srcSolrClient;
    private HttpSolrClient dstSolrClient;
    private boolean verbose;
    private int maxDocsPerQuery;

    private List<String> ignoredFields = Arrays.asList("_version_");

    public SolrRecordTransporter(String srcSolrHost, String dstSolrHost, int maxDocsPerQuery, boolean verbose) {
        this.verbose = verbose;
        this.maxDocsPerQuery = maxDocsPerQuery;
        this.srcSolrClient = new HttpSolrClient.Builder(srcSolrHost)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        this.dstSolrClient = new HttpSolrClient.Builder(dstSolrHost)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }

    public void transport(String rootUuid) throws IOException, SolrServerException {
        String allPartsQueryStr = "root_pid:\"" + rootUuid.trim() + "\"";
        SolrQuery query = new SolrQuery(allPartsQueryStr);
        query.addField("PID");

        long numFound = querySrcForNumFound(query);
        query.setFields("*");

        if (numFound != 0) {
            if (numFound > maxDocsPerQuery) {
                query.setRows(maxDocsPerQuery);
                transportByCursor(query);
            } else {
                query.setRows(Math.toIntExact(numFound));
                transportByRequest(query);
            }
            dstSolrClient.commit();
        }
    }

    public void close() throws IOException {
        srcSolrClient.close();
        dstSolrClient.close();
    }

    private void transportByCursor(SolrQuery query) throws IOException, SolrServerException {
        query.setSort(SolrQuery.SortClause.asc("PID"));
        String cursorMark = CursorMarkParams.CURSOR_MARK_START;
        boolean done = false;
        while (!done) {
            query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
            QueryResponse rsp = querySrcSolrClient(query);
            SolrDocumentList solrDocs = rsp.getResults();

            indexToDstSolr(solrDocs);

            String nextCursorMark = rsp.getNextCursorMark();
            if (cursorMark.equals(nextCursorMark)) {
                done = true;
            }
            cursorMark = nextCursorMark;
        }
    }

    private void transportByRequest(SolrQuery query) throws IOException, SolrServerException {
        SolrDocumentList solrDocs = querySrcForSolrDocList(query);
        indexToDstSolr(solrDocs);
    }

    private void indexToDstSolr(SolrDocumentList docs) throws IOException, SolrServerException {
        List<SolrInputDocument> inputDocs = convertToInputDocs(docs);
        for (SolrInputDocument inputDoc : inputDocs) {
            dstSolrClient.add(inputDoc);
            if (verbose) {
                System.out.println((String) inputDoc.getFieldValue("PID"));
            }
        }
    }

    private List<SolrInputDocument> convertToInputDocs(SolrDocumentList docs) {
        List<SolrInputDocument> inputDocs = new ArrayList<>();
        for (SolrDocument doc : docs) {
            SolrInputDocument inputDoc = new SolrInputDocument();
            Collection<String> srcDocFieldNames = doc.getFieldNames();
            for (String fieldName : srcDocFieldNames) {
                if (!ignoredFields.contains(fieldName)) {
                    inputDoc.addField(fieldName, doc.getFieldValue(fieldName));
                }
            }
            inputDocs.add(inputDoc);
        }
        return inputDocs;
    }

    private long querySrcForNumFound(SolrQuery query) throws IOException, SolrServerException {
        query.setRows(0);
        SolrDocumentList docs = querySrcForSolrDocList(query);
        return docs.getNumFound();
    }

    private SolrDocumentList querySrcForSolrDocList(SolrQuery query) throws IOException, SolrServerException {
        QueryResponse response = querySrcSolrClient(query);
        return response.getResults();
    }

    private QueryResponse querySrcSolrClient(SolrQuery query) throws IOException, SolrServerException {
        return srcSolrClient.query(query);
    }
}
