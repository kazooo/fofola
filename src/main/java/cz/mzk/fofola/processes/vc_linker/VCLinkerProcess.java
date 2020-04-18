package cz.mzk.fofola.processes.vc_linker;

import cz.mzk.fofola.processes.core.models.Process;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class VCLinkerProcess extends Process {

    private String vcUuid;
    private List<String> rootUuids;
    private String solrHost;
    private String fedoraHost;
    private String fedoraUser;
    private String fedoraPswd;

    public VCLinkerProcess(LinkedHashMap<String, Object> params) throws IOException {
        super(params);
    }

    @Override
    @SuppressWarnings("unchecked") // cast Object to List<String> for rootUuids
    protected void setupParams(LinkedHashMap<String, Object> params) {
        vcUuid = (String) params.get("vc_uuid");
        solrHost = (String) params.get("solr_host");
        fedoraHost = (String) params.get("fedora_host");
        fedoraUser = (String) params.get("fedora_user");
        fedoraPswd = (String) params.get("fedora_pswd");
        rootUuids = (List<String>) params.get("root_uuids");
    }

    @Override
    public void process() throws Exception {
        KrameriusVCLinker vcLinker  = new KrameriusVCLinker(
                fedoraHost, fedoraUser, fedoraPswd,
                solrHost, 1500, logger
        );
        for (String rootUuid : rootUuids) {
            vcLinker.linkRootAndChildrenToVc(vcUuid, rootUuid);
        }
        vcLinker.close();
    }
}
