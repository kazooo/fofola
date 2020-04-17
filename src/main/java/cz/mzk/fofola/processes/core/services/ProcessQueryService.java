package cz.mzk.fofola.processes.core.services;

import cz.mzk.fofola.processes.core.models.ProcessDTO;
import cz.mzk.fofola.processes.core.queries.FindAllProcessQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProcessQueryService {

    private final QueryGateway queryGateway;

    public ProcessQueryService(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    public CompletableFuture<List<ProcessDTO>> findAllProcess() {
        return queryGateway.query(
                new FindAllProcessQuery(),
                ResponseTypes.multipleInstancesOf(ProcessDTO.class)
        );
    }
}
