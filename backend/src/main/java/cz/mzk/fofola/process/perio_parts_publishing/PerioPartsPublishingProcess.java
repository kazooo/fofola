package cz.mzk.fofola.process.perio_parts_publishing;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;
import cz.mzk.fofola.model.solr.SearchDoc;
import cz.mzk.fofola.repository.SolrRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class PerioPartsPublishingProcess extends Process {

    private final KrameriusApi krameriusApi;
    private final SolrRepository<SearchDoc> solrSearchRepository;
    private final List<String> rootUuids;

    @SuppressWarnings("unchecked")
    public PerioPartsPublishingProcess(ProcessParams params) throws IOException {
        super(params);
        final Map<String, ?> data = params.getData();
        rootUuids = (List<String>) data.get("root_uuids");
        krameriusApi = params.getKrameriusApi();
        solrSearchRepository = params.getSolrSearchRepository();
    }

    @Override
    public TerminationReason process() throws Exception {
        final PerioPartsPublisher publisher = new PerioPartsPublisher(krameriusApi, solrSearchRepository, logger);
        for (final String rootUuid : rootUuids) {
            publisher.checkPartsAndMakePublic(rootUuid);
            if (Thread.interrupted()) {
                return TerminationReason.USER_COMMAND;
            }
        }
        return null;
    }
}
