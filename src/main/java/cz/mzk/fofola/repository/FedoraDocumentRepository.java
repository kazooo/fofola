package cz.mzk.fofola.repository;

import cz.mzk.fofola.api.FedoraApi;
import cz.mzk.fofola.model.FedoraDocument;
import cz.mzk.fofola.service.XMLService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@AllArgsConstructor
public class FedoraDocumentRepository {

    private final XMLService xmlService;
    private final FedoraApi fedoraApi;

    public FedoraDocument getByUuid(String uuid) {
        return xmlService.parseFedoraDocument(fedoraApi.getByUuid(uuid));
    }
}
