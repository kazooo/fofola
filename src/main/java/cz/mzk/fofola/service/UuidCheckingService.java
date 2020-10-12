package cz.mzk.fofola.service;

import cz.mzk.fofola.model.FedoraDocument;
import cz.mzk.fofola.model.UuidStateResponse;
import cz.mzk.fofola.model.SolrDocument;
import cz.mzk.fofola.repository.FedoraDocumentRepository;
import cz.mzk.fofola.repository.SolrDocumentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class UuidCheckingService {

    private final SolrDocumentRepository solrRepository;
    private final FedoraDocumentRepository fedoraRepository;

    public UuidStateResponse checkUuidState(String uuid) {
        UuidStateResponse response = new UuidStateResponse(uuid);
        SolrDocument solrDoc = solrRepository.getByUuid(uuid);
        FedoraDocument fedoraDoc = fedoraRepository.getFedoraDocByUuid(uuid);

        response.setIndexed(solrDoc != null);
        response.setStored(fedoraDoc != null);

        if (solrDoc != null) {
            if (solrDoc.getModifiedDate() != null) {
                response.setSolrModifiedDate(solrDoc.getModifiedDate());
            }
            response.setAccessibilityInSolr(solrDoc.getAccessibility());
            response.setRootTitle(solrDoc.getRootTitle());
        }

        if (fedoraDoc != null) {
            response.setAccessibilityInFedora(fedoraDoc.getAccesibility());
            response.setModel(fedoraDoc.getModel());
            response.setFedoraModifiedDate(fedoraDoc.getModifiedDateStr());
            response.setImgUrl(fedoraDoc.getImageUrl());
        }

        return response;
    }
}
