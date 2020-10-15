package cz.mzk.fofola.process.internal.perio_parts_publishing;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class PerioPartsPublishingProcess extends Process {

    private final List<String> rootUuids;
    private final String solrHost;
    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPswd;

    @SuppressWarnings("unchecked")
    public PerioPartsPublishingProcess(ProcessParams params) throws IOException {
        super(params);
        FofolaConfiguration fofolaConfig = params.getConfig();
        Map<String, ?> data = params.getData();

        rootUuids = (List<String>) data.get("root_uuids");
        solrHost = fofolaConfig.getSolrHost();
        fedoraHost = fofolaConfig.getFedoraHost();
        fedoraUser = fofolaConfig.getFedoraUser();
        fedoraPswd = fofolaConfig.getFedoraPswd();
    }

    @Override
    public TerminationReason process() throws Exception {
        PerioPartsPublisher publisher = new PerioPartsPublisher(
                fedoraHost, fedoraUser, fedoraPswd, solrHost, 1500, logger
        );
        for (String rootUuid : rootUuids) {
            publisher.checkPartsAndMakePublic(rootUuid);
            if (Thread.interrupted()) {
                return TerminationReason.USER_COMMAND;
            }
        }
        publisher.close();
        return null;
    }
}
