package cz.mzk.fofola.processes;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;

import java.io.IOException;
import java.util.function.Consumer;

public class SolrUtils {

    public static void fetchByCursorIfMoreDocsElseByRequestAndApply(
            SolrQuery query, SolrClient solrClient,
            Consumer<SolrDocument> consumer, int maxDocs) throws IOException, SolrServerException {
        long numFound = queryForNumFound(query, solrClient);
        if (numFound != 0) {
            if (numFound > maxDocs) {
                query.setRows(maxDocs);
                fetchByCursorAndApply(query, solrClient, consumer);
            } else {
                query.setRows(Math.toIntExact(numFound));
                fetchByRequestAndApply(query, solrClient, consumer);
            }
        }
    }

    public static void fetchByCursorAndApply(SolrQuery query, SolrClient solrClient,
                                             Consumer<SolrDocument> consumer)
            throws IOException, SolrServerException {
        query.setSort(SolrQuery.SortClause.asc("PID"));
        String cursorMark = CursorMarkParams.CURSOR_MARK_START;
        boolean done = false;
        while (!done) {
            query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
            QueryResponse rsp = querySolrClient(query, solrClient);
            SolrDocumentList solrDocs = rsp.getResults();
            forEachSolrDoc(solrDocs, consumer);

            String nextCursorMark = rsp.getNextCursorMark();
            if (cursorMark.equals(nextCursorMark)) {
                done = true;
            }
            cursorMark = nextCursorMark;
        }
    }

    public static void fetchByRequestAndApply(SolrQuery query, SolrClient solrClient,
                                      Consumer<SolrDocument> consumer)
            throws IOException, SolrServerException {
        SolrDocumentList solrDocs = queryForSolrDocList(query, solrClient);
        forEachSolrDoc(solrDocs, consumer);
    }

    public static void forEachSolrDoc(SolrDocumentList docs, Consumer<SolrDocument> consumer) {
        for (SolrDocument doc : docs) {
            consumer.accept(doc);
        }
    }

    public static long queryForNumFound(SolrQuery query, SolrClient solrClient)
            throws IOException, SolrServerException {
        int oldRows = query.getRows();
        query.setRows(0);
        SolrDocumentList docs = queryForSolrDocList(query, solrClient);
        query.setRows(oldRows);
        return docs.getNumFound();
    }

    public static SolrDocumentList queryForSolrDocList(SolrQuery query, SolrClient solrClient)
            throws IOException, SolrServerException {
        QueryResponse response = querySolrClient(query, solrClient);
        return response.getResults();
    }

    public static QueryResponse querySolrClient(SolrQuery query, SolrClient solrClient)
            throws IOException, SolrServerException {
        return solrClient.query(query);
    }
}
