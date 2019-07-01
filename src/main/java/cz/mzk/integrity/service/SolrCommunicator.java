package cz.mzk.integrity.service;

import cz.mzk.integrity.model.SolrDocument;
import cz.mzk.integrity.repository.SolrDocumentRepository;
import org.apache.solr.client.solrj.response.FacetField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.result.FacetEntry;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SolrCommunicator {

    private final SolrDocumentRepository solrRepository;

    public SolrCommunicator(SolrDocumentRepository solrRepository) {
        this.solrRepository = solrRepository;
    }

    public SolrDocument getSolrDocByUuid(String uuid) {
        List<SolrDocument> docs = solrRepository.findByUuid(uuid);

        if (docs == null || docs.isEmpty()) {
            throw new NoSuchElementException("Can't find any Solr document with uuid: " + uuid);
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
}
