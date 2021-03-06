package cz.mzk.fofola.process.vc_linker;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
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
                solrHost, logger
        );
        if (mode.equals(MODE_LINK)) {
            logger.info("Link root uuids (" + rootUuids.size() + ") to " + vcUuid);
            for (String rootUuid : rootUuids) {
                vcLinker.linkToVcByRootUuid(vcUuid, rootUuid);
                if (Thread.interrupted()) {
                    return TerminationReason.USER_COMMAND;
                }
            }
        } else if (mode.equals(MODE_UNLINK)) {
            logger.info("Unlink root uuids (" + rootUuids.size() + ") from " + vcUuid);
            for (String rootUuid : rootUuids) {
                vcLinker.unlinkFromVcByRootUuid(vcUuid, rootUuid);
                if (Thread.interrupted()) {
                    return TerminationReason.USER_COMMAND;
                }
            }
        } else {
            throw new IllegalAccessException("Unknown mode \"" + mode + "\"");
        }
        vcLinker.commitAndClose();
        return null;
    }
}
