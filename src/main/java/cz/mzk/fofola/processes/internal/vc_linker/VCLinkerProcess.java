package cz.mzk.fofola.processes.internal.vc_linker;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.processes.core.models.Process;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;


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
    public VCLinkerProcess(LinkedHashMap<String, Object> params,
                           FofolaConfiguration fofolaConfiguration) throws IOException {
        super(params);
        mode = (String) params.get("mode");
        vcUuid = (String) params.get("vc_uuid");
        rootUuids = (List<String>) params.get("root_uuids");
        solrHost = fofolaConfiguration.getSolrHost();
        fedoraHost = fofolaConfiguration.getFedoraHost();
        fedoraUser = fofolaConfiguration.getFedoraUser();
        fedoraPswd = fofolaConfiguration.getFedoraPswd();
    }

    @Override
    public void process() throws Exception {
        KrameriusVCLinker vcLinker  = new KrameriusVCLinker(
                fedoraHost, fedoraUser, fedoraPswd,
                solrHost, 1500, logger
        );
        if (mode.equals(MODE_LINK)) {
            for (String rootUuid : rootUuids) {
                vcLinker.linkRootAndChildrenToVc(vcUuid, rootUuid);
                if (Thread.interrupted()) break;
            }
        } else if (mode.equals(MODE_UNLINK)) {
            for (String rootUuid : rootUuids) {
                vcLinker.unlinkRootAndChildrenFromVc(vcUuid, rootUuid);
                if (Thread.interrupted()) break;
            }
        } else throw new IllegalAccessException("Unknown mode \"" + mode + "\"");
        vcLinker.commitAndClose();
    }
}
