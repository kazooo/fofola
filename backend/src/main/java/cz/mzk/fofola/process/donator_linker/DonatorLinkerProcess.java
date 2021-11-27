package cz.mzk.fofola.process.donator_linker;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class DonatorLinkerProcess extends Process {

    private final String donator;
    private final String mode;
    private final List<String> rootUuids;
    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPswd;
    private final String solrHost;

    @SuppressWarnings("unchecked")
    public DonatorLinkerProcess(ProcessParams params) throws IOException {
        super(params);
        FofolaConfiguration fofolaConfig = params.getConfig();
        Map<String, ?> data = params.getData();

        donator = (String) data.get("donator");
        mode = (String) data.get("mode");
        rootUuids = (List<String>) data.get("uuids");
        fedoraHost = fofolaConfig.getFedoraHost();
        fedoraUser = fofolaConfig.getFedoraUser();
        fedoraPswd = fofolaConfig.getFedoraPswd();
        solrHost = fofolaConfig.getSolrHost();
    }

    @Override
    public TerminationReason process() throws Exception {
        DonatorLinker donatorLinker = new DonatorLinker(
                fedoraHost, fedoraUser, fedoraPswd, solrHost, 1500, logger
        );
        for (String rootUuid : rootUuids) {
            switch (mode) {
                case "link": donatorLinker.link(rootUuid, donator); break;
                case "unlink": donatorLinker.unlink(rootUuid, donator); break;
                default: throw new IllegalArgumentException("Bad mode \""+mode+"\" for process DonatorLinkerProcess!");
            }
            if (Thread.interrupted()) {
                return TerminationReason.USER_COMMAND;
            }
        }
        donatorLinker.commitAndClose();
        return null;
    }
}
