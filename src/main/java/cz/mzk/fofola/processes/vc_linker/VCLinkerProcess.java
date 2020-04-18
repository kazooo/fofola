package cz.mzk.fofola.processes.vc_linker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.mzk.fofola.processes.core.models.Process;

import java.io.IOException;
import java.lang.reflect.Type;
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
    protected void setupParams(LinkedHashMap<String, Object> params) {
        vcUuid = (String) params.get("vc_uuid");
        solrHost = (String) params.get("solr_host");
        fedoraHost = (String) params.get("fedora_host");
        fedoraUser = (String) params.get("fedora_user");
        fedoraPswd = (String) params.get("fedora_pswd");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>(){}.getType();
        rootUuids = gson.fromJson((String) params.get("root_uuids"), listType);
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
