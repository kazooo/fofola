package cz.mzk.fofola.processes.internal.perio_parts_publishing;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.processes.core.models.Process;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class PerioPartsPublishingProcess extends Process {

    private final List<String> rootUuids;
    private final String solrHost;
    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPswd;

    public PerioPartsPublishingProcess(LinkedHashMap<String, Object> params,
                                       FofolaConfiguration fofolaConfiguration) throws IOException {
        super(params);
        rootUuids = (List<String>) params.get("root_uuids");
        solrHost = fofolaConfiguration.getSolrHost();
        fedoraHost = fofolaConfiguration.getFedoraHost();
        fedoraUser = fofolaConfiguration.getFedoraUser();
        fedoraPswd = fofolaConfiguration.getFedoraPswd();
    }

    @Override
    public void process() throws Exception {
        PerioPartsPublisher publisher = new PerioPartsPublisher(
                fedoraHost, fedoraUser, fedoraPswd, solrHost, 1500, logger
        );
        for (String rootUuid : rootUuids) {
            publisher.checkPartsAndMakePublic(rootUuid);
            if (Thread.interrupted()) break;
        }
        publisher.close();
    }
}