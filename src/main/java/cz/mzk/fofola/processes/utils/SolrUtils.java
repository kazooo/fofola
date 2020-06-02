package cz.mzk.fofola.processes.utils;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CursorMarkParams;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SolrUtils {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static SolrClient buildClient(String solrHost) {
        return new HttpSolrClient.Builder(solrHost)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }

    public static void iterateByCursorIfMoreDocsElseBySingleRequestAndApply(
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
        Integer oldRows = query.getRows();
        query.setRows(0);
        SolrDocumentList docs = queryForSolrDocList(query, solrClient);
        query.setRows(oldRows);
        return docs.getNumFound();
    }

    public static SolrDocument queryFirstSolrDoc(SolrQuery solrQuery, SolrClient solrClient)
            throws IOException, SolrServerException {
        return queryForSolrDocList(solrQuery, solrClient).get(0);
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

    public static void makePublic(String uuid, SolrClient solrClient) throws IOException, SolrServerException {
        changeAccessibility(uuid, solrClient, "public");
    }

    public static void makePrivate(String uuid, SolrClient solrClient) throws IOException, SolrServerException {
        changeAccessibility(uuid, solrClient, "private");
    }

    private static void changeAccessibility(String uuid, SolrClient solrClient, String accessibility)
            throws IOException, SolrServerException {
        SolrInputDocument inputDoc = new SolrInputDocument();
        inputDoc.addField("PID", uuid);

        Map<String, String> accessibilityFieldModifier = new HashMap<>(1);
        accessibilityFieldModifier.put("set", accessibility);
        inputDoc.addField("dostupnost", accessibilityFieldModifier);

        Map<String, String> modifiedDateFieldModifier = new HashMap<>(1);
        modifiedDateFieldModifier.put("set", dateFormat.format(new Date()));
        inputDoc.addField("modified_date", modifiedDateFieldModifier);

        solrClient.add(inputDoc);
    }
}
