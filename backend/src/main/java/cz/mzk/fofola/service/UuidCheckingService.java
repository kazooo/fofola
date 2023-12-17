package cz.mzk.fofola.service;

import cz.mzk.fofola.model.UuidStateResponse;
import cz.mzk.fofola.model.solr.SearchDoc;
import cz.mzk.fofola.repository.SolrRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UuidCheckingService {

    private final SolrRepository<SearchDoc> solrRepository;

    public UuidStateResponse checkUuidState(String uuid) {
        UuidStateResponse response = new UuidStateResponse(uuid);
        SearchDoc solrDoc = solrRepository.getByUuid(uuid);

        response.setIndexed(solrDoc != null);

        if (solrDoc != null) {
            if (solrDoc.getModified() != null) {
                response.setSolrModifiedDate(solrDoc.getModified());
            }
            response.setAccessibilityInSolr(solrDoc.getAccessibility().getName());
            response.setModel(solrDoc.getModel());
            response.setRootTitle(solrDoc.getRootTitle());
        }

        return response;
    }
}
