package cz.mzk.fofola.processes.vc_linker;

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

    public VCLinkerProcess(LinkedHashMap<String, Object> params,
                           FofolaConfiguration fofolaConfiguration) throws IOException {
        super(params);
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
        for (String rootUuid : rootUuids) {
            vcLinker.linkRootAndChildrenToVc(vcUuid, rootUuid);
            if (Thread.interrupted()) break;
        }
        vcLinker.close();
    }
}
