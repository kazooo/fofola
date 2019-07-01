package cz.mzk.integrity.service;

import cz.mzk.integrity.model.SolrDocument;
import cz.mzk.integrity.repository.SolrDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
}
