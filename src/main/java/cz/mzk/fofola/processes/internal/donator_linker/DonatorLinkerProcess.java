package cz.mzk.fofola.processes.internal.donator_linker;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.processes.core.models.Process;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class DonatorLinkerProcess extends Process {

    private final String donator;
    private final String mode;
    private final List<String> rootUuids;
    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPswd;
    private final String solrHost;

    public DonatorLinkerProcess(LinkedHashMap<String, Object> params,
                                FofolaConfiguration fofolaConfig) throws IOException {
        super(params);
        donator = (String) params.get("donator");
        mode = (String) params.get("mode");
        rootUuids = (List<String>) params.get("root_uuids");
        fedoraHost = fofolaConfig.getFedoraHost();
        fedoraUser = fofolaConfig.getFedoraUser();
        fedoraPswd = fofolaConfig.getFedoraPswd();
        solrHost = fofolaConfig.getSolrHost();
    }

    @Override
    public void process() throws Exception {
        DonatorLinker donatorLinker = new DonatorLinker(
                fedoraHost, fedoraUser, fedoraPswd, solrHost, 1500, logger
        );
        for (String rootUuid : rootUuids) {
            switch (mode) {
                case "link": donatorLinker.link(rootUuid, donator); break;
                case "unlink": donatorLinker.unlink(rootUuid, donator); break;
                default: throw new IllegalArgumentException("Bad mode \""+mode+"\" for process DonatorLinkerProcess!");
            }
            if (Thread.interrupted()) break;
        }
        donatorLinker.commitAndClose();
    }
}
