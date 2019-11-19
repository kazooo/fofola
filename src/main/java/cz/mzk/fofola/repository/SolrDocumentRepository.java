package cz.mzk.fofola.repository;

import cz.mzk.fofola.model.SolrDocument;
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

    @Query(fields = {SolrDocument.ID, SolrDocument.PARENT_PID, SolrDocument.RELS_EXT_INDEX,
            SolrDocument.MODEL, SolrDocument.DC_TITLE, SolrDocument.VISIBILITY})
    public List<SolrDocument> findByRootPid(String uuid);

    public List<SolrDocument> findByParentPids(String uuid);
}
