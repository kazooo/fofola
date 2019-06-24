package cz.mzk.integrity.repository;

import cz.mzk.integrity.model.SolrDocument;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface SolrDocumentRepository extends SolrCrudRepository<SolrDocument, String> {

    public List<SolrDocument> findByUuid(String uuid);
}
