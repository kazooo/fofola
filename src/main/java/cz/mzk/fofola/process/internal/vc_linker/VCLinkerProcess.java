package cz.mzk.fofola.process.internal.vc_linker;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.process.management.Process;
import cz.mzk.fofola.process.management.ProcessParams;
import cz.mzk.fofola.process.constants.TerminationReason;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class VCLinkerProcess extends Process {

    private final String vcUuid;
    private final List<String> rootUuids;
    private final String solrHost;
    private final String fedoraHost;
    private final String fedoraUser;
    private final String fedoraPswd;

    private final String mode;
    private static final String MODE_LINK = "link";
    private static final String MODE_UNLINK = "unlink";

    @SuppressWarnings("unchecked")
    public VCLinkerProcess(ProcessParams params) throws IOException {
        super(params);
        FofolaConfiguration fofolaConfig = params.getConfig();
        Map<String, ?> data = params.getData();

        mode = (String) data.get("mode");
        vcUuid = (String) data.get("vc_uuid");
        rootUuids = (List<String>) data.get("root_uuids");
        solrHost = fofolaConfig.getSolrHost();
        fedoraHost = fofolaConfig.getFedoraHost();
        fedoraUser = fofolaConfig.getFedoraUser();
        fedoraPswd = fofolaConfig.getFedoraPswd();
    }

    @Override
    public TerminationReason process() throws Exception {
        KrameriusVCLinker vcLinker  = new KrameriusVCLinker(
                fedoraHost, fedoraUser, fedoraPswd,
                solrHost, 1500, logger
        );
        if (mode.equals(MODE_LINK)) {
            for (String rootUuid : rootUuids) {
                vcLinker.linkRootAndChildrenToVc(vcUuid, rootUuid);
                if (Thread.interrupted()) {
                    return TerminationReason.USER_COMMAND;
                }
            }
        } else if (mode.equals(MODE_UNLINK)) {
            for (String rootUuid : rootUuids) {
                vcLinker.unlinkRootAndChildrenFromVc(vcUuid, rootUuid);
                if (Thread.interrupted()) {
                    return TerminationReason.USER_COMMAND;
                }
            }
        } else throw new IllegalAccessException("Unknown mode \"" + mode + "\"");
        vcLinker.commitAndClose();
        return null;
    }
}
