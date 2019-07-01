package cz.mzk.integrity.repository;

import cz.mzk.integrity.model.SolrDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface SolrDocumentRepository extends SolrCrudRepository<SolrDocument, String> {

    public List<SolrDocument> findByUuid(String uuid);

    @Query(value = "*:*")
    @Facet(fields = {SolrDocument.MODEL})
    public FacetPage<SolrDocument> facetByModels(Pageable page);
}
