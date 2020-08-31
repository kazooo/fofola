package cz.mzk.fofola.processes.utils;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SolrUtils {

    public static final String ALL_FIELDS = "*";
    public static final String ALL_VALUES = "*";
    public static final String UUID_FIELD_NAME = "PID";
    public static final String DATE_STR_FIELD_NAME = "datum_str";
    public static final String DATE_BEGIN_FIELD_NAME = "datum_begin";
    public static final String DATE_END_FIELD_NAME = "datum_end";
    public static final String DATE_FIELD_NAME = "datum";
    public static final String YEAR_FIELD_NAME = "rok";
    public static final String ROOT_PID_FIELD_NAME = "root_pid";
    public static final String MODEL_FIELD_NAME = "fedora.model";
    public static final String LOCATION_FIELD_NAME = "location";
    public static final String MODIFIED_DATE_FIELD_NAME = "modified_date";
    public static final String DETAILS_FIELD_NAME = "details";
    public static final String COLLECTION_FIELD_NAME = "collection";

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

    public static String wrapQueryStr(String field, String value) {
        return field + ":\"" + value + "\"";
    }

    public static void insertAddUpdate(SolrInputDocument inputDoc, String fieldName, Object fieldValue) {
        Map<String,Object> fieldModifier = createFieldModifier("add", fieldValue);
        inputDoc.addField(fieldName, fieldModifier);
    }

    public static void insertSetUpdate(SolrInputDocument inputDoc, String fieldName, Object fieldValue) {
        Map<String,Object> fieldModifier = createFieldModifier("set", fieldValue);
        inputDoc.addField(fieldName, fieldModifier);
    }

    private static Map<String, Object> createFieldModifier(String modifier, Object fieldValue) {
        Map<String, Object> fieldModifier = new HashMap<>(1);
        fieldModifier.put(modifier, fieldValue);
        return fieldModifier;
    }

    public static void insertModifiedDateNow(SolrInputDocument inputDoc) {
        inputDoc.removeField(SolrUtils.MODIFIED_DATE_FIELD_NAME);
        insertSetUpdate(inputDoc, SolrUtils.MODIFIED_DATE_FIELD_NAME, dateFormat.format(new Date()));
    }

    public static void fetchFacetApplyConsumer(SolrClient solrClient, SolrQuery query, Consumer<FacetField> consumer)
            throws IOException, SolrServerException {
        QueryResponse response = solrClient.query(query);
        List<FacetField> facets = response.getFacetFields();
        for (FacetField facet : facets) {
            consumer.accept(facet);
        }
    }
}
