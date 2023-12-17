package cz.mzk.fofola.repository;

import cz.mzk.fofola.configuration.AppProperties;
import cz.mzk.fofola.utils.solr.SolrDocMapper;
import lombok.SneakyThrows;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.function.Consumer;

public class SolrRepository<T> {

    private final SolrClient client;
    private final Class<T> type;
    private final int maxDocs;

    public SolrRepository(final SolrClient solrClient, final AppProperties props, final Class<T> docType) {
        client = solrClient;
        type = docType;
        maxDocs = props.getSolrMaxDocToFetch();
    }

    @SneakyThrows
    public T getByUuid(final String uuid) {
        SolrDocument doc = client.getById(uuid);
        return SolrDocMapper.convert(doc, type);
    }

    @SneakyThrows
    public T getFirstByQuery(final SolrQuery query) {
        SolrDocumentList docs = queryForSolrDocList(query);
        if (docs.isEmpty()) {
            return null;
        }
        return SolrDocMapper.convert(docs.get(0), type);
    }

    @SneakyThrows
    public void paginateByCursor(SolrQuery query, Consumer<T> consumer) {
        long numFound = queryForNumFound(query);
        if (numFound != 0) {
            if (numFound > maxDocs) {
                query.setRows(maxDocs);
                fetchByCursorAndApply(query, consumer);
            } else {
                query.setRows(Math.toIntExact(numFound));
                fetchByRequestAndApply(query, consumer);
            }
        }
    }

    @SneakyThrows
    public void fetchByCursorAndApply(SolrQuery query, Consumer<T> consumer) {
        query.setSort(SolrQuery.SortClause.asc(SolrDocMapper.getIdFieldName(type)));
        String cursorMark = CursorMarkParams.CURSOR_MARK_START;
        boolean done = false;
        while (!done) {
            query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
            QueryResponse rsp = querySolrClient(query);
            SolrDocumentList solrDocs = rsp.getResults();
            forEachSolrDoc(solrDocs, consumer);

            String nextCursorMark = rsp.getNextCursorMark();
            if (cursorMark.equals(nextCursorMark)) {
                done = true;
            }
            cursorMark = nextCursorMark;
        }
    }

    public void fetchByRequestAndApply(SolrQuery query, Consumer<T> consumer)
            throws IOException, SolrServerException {
        SolrDocumentList solrDocs = queryForSolrDocList(query);
        forEachSolrDoc(solrDocs, consumer);
    }

    public void forEachSolrDoc(SolrDocumentList docs, Consumer<T> consumer) {
        for (SolrDocument doc : docs) {
            consumer.accept(SolrDocMapper.convert(doc, type));
        }
    }

    public long queryForNumFound(SolrQuery query)
            throws IOException, SolrServerException {
        Integer oldRows = query.getRows();
        query.setRows(0);
        SolrDocumentList docs = queryForSolrDocList(query);
        query.setRows(oldRows);
        return docs.getNumFound();
    }

    private SolrDocumentList queryForSolrDocList(SolrQuery query) throws IOException, SolrServerException {
        QueryResponse response = querySolrClient(query);
        return response.getResults();
    }

    private QueryResponse querySolrClient(SolrQuery query) throws IOException, SolrServerException {
        return client.query(query);
    }

    @PreDestroy
    @SneakyThrows
    public void close() {
        client.close();
    }
}
