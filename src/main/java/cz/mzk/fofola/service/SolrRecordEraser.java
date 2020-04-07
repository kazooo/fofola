package cz.mzk.fofola.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

public class SolrRecordEraser {

    private HttpSolrClient solrClient;
    private int maxDocsPerQuery;
    private boolean verbose;

    public SolrRecordEraser(String solrHost, int maxDocsPerQuery, boolean verbose) {
        this.solrClient = new HttpSolrClient.Builder(solrHost)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        this.maxDocsPerQuery = maxDocsPerQuery;
        this.verbose = verbose;
    }

    public void archiveAndEraseWithChildren(String rootUuid) throws IOException, SolrServerException {
        String allPartsQueryStr = "root_pid:\"" + rootUuid.trim() + "\"";
        SolrQuery query = new SolrQuery(allPartsQueryStr);
        query.addField("PID");

        long numFound = queryForNumFound(query);

        if (numFound != 0) {
            String fileName = createArchiveFileName(rootUuid);
            createArchiveFile(fileName);

            try (PrintWriter writer = new PrintWriter(new File(fileName))) {
                if (numFound > maxDocsPerQuery) {
                    query.setRows(maxDocsPerQuery);
                    archiveAndEraseByCursor(writer, query);
                } else {
                    query.setRows(Math.toIntExact(numFound));
                    archiveAndEraseByRequest(writer, query);
                }
            }

            solrClient.commit();
        }
    }

    public void close() throws IOException {
        solrClient.close();
    }

    private void archiveAndEraseByCursor(PrintWriter writer, SolrQuery query)
            throws IOException, SolrServerException {
        query.setSort(SolrQuery.SortClause.asc("PID"));
        String cursorMark = CursorMarkParams.CURSOR_MARK_START;
        boolean done = false;
        while (!done) {
            query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
            QueryResponse rsp = querySolrClient(query);

            SolrDocumentList docs = rsp.getResults();
            archiveSolrDocList(writer, docs);
            eraseUuidsFromIndex(docs);

            String nextCursorMark = rsp.getNextCursorMark();
            if (cursorMark.equals(nextCursorMark)) {
                done = true;
            }
            cursorMark = nextCursorMark;
        }
    }

    private void archiveAndEraseByRequest(PrintWriter writer, SolrQuery query)
            throws IOException, SolrServerException {
        SolrDocumentList solrDocs = queryForSolrDocList(query);
        archiveSolrDocList(writer, solrDocs);
        eraseUuidsFromIndex(solrDocs);
    }

    private void archiveSolrDocList(PrintWriter writer, SolrDocumentList docs) {
        for (SolrDocument doc : docs) {
            String uuid = (String) doc.getFieldValue("PID");
            writer.println(uuid);
            if (verbose) { System.out.println(uuid); }
        }
    }

    private void eraseUuidsFromIndex(SolrDocumentList docs) {
        for (SolrDocument doc : docs) {
            try {
                solrClient.deleteById((String) doc.getFieldValue("PID"));
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String createArchiveFileName(String rootUuid) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return rootUuid + " " + timestamp.toString();
    }

    private void createArchiveFile(String fileName) throws IOException {
        File archiveFile = new File(fileName);
        archiveFile.createNewFile();
    }

    private long queryForNumFound(SolrQuery query) throws IOException, SolrServerException {
        query.setRows(0);
        SolrDocumentList docs = queryForSolrDocList(query);
        return docs.getNumFound();
    }

    private SolrDocumentList queryForSolrDocList(SolrQuery query) throws IOException, SolrServerException {
        QueryResponse response = querySolrClient(query);
        return response.getResults();
    }

    private QueryResponse querySolrClient(SolrQuery query) throws IOException, SolrServerException {
        return solrClient.query(query);
    }
}
