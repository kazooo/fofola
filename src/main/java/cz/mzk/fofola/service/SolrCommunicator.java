package cz.mzk.fofola.service;

import cz.mzk.fofola.model.SolrDocument;
import cz.mzk.fofola.repository.SolrDocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.data.solr.core.query.result.FacetEntry;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class SolrCommunicator {

    private final SolrDocumentRepository solrRepository;
    private final SolrTemplate solrTemplate;
    private final String collectionName = "kramerius";

    public SolrDocument getSolrDocByUuid(String uuid) {
        List<SolrDocument> docs = solrRepository.findByUuid(uuid);
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new IllegalStateException("Can not be more than one document with uuid: " + uuid);
        }
        return docs.get(0);
    }

    public Map<String, Long> facetSolrDocByModels() {
        FacetPage<SolrDocument> docs = solrRepository.facetByModels(PageRequest.of(0, 1));
        Page<FacetFieldEntry> page = docs.getFacetResultPage(SolrDocument.MODEL);
        Map<String, Long> modelCount = new HashMap<>();
        for (FacetEntry facetEntry : page.getContent()) {
            modelCount.put(facetEntry.getValue(), facetEntry.getValueCount());
        }
        modelCount.put("total", docs.getTotalElements());
        return modelCount;
    }

    public List<SolrDocument> cursorQuery(String collectionName,
                                          Query query) {
        List<SolrDocument> result = new ArrayList<>();
        Cursor<SolrDocument> docs =
                solrTemplate.queryForCursor(collectionName, query, SolrDocument.class);
        while (docs.hasNext()) {
            SolrDocument doc = docs.next();
            result.add(doc);
        }
        return result;
    }

    public Cursor<SolrDocument> getDocCursor(String collectionName,
                                             Query query) {
        return solrTemplate.queryForCursor(collectionName, query, SolrDocument.class);
    }

    public long docCount(SimpleQuery query) {
        return solrTemplate.count(collectionName, query);
    }

    public List<SolrDocument> getSolrDocsByRootPid(String rootUuid) {
        return solrRepository.findByRootPid(rootUuid);
    }

    public List<SolrDocument> getSolrDocsByParentPid(String parentUuid) {
        return solrRepository.findByParentPids(parentUuid);
    }

    public long getDocNumByRootPid(String rootUuid) {
        SimpleQuery query = new SimpleQuery(SolrDocument.ROOT_PID + ":\"" + rootUuid + "\"");
        query.setRows(0);
        return solrTemplate.count(collectionName, query);
    }
}
