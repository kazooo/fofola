package cz.mzk.fofola.process.vc_linker;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class VCLinkerProcess extends Process {

    private final String vcUuid;
    private final List<String> rootUuids;
    private final FofolaConfiguration configuration;

    private final String mode;
    private static final String MODE_LINK = "LINK_MODE";
    private static final String MODE_UNLINK = "UNLINK_MODE";

    @SuppressWarnings("unchecked")
    public VCLinkerProcess(ProcessParams params) throws IOException {
        super(params);
        configuration = params.getConfig();
        Map<String, ?> data = params.getData();
        mode = (String) data.get("mode");
        vcUuid = (String) data.get("vcUuid");
        rootUuids = (List<String>) data.get("uuids");
    }

    @Override
    public TerminationReason process() throws Exception {
        KrameriusVCLinker vcLinker  = new KrameriusVCLinker(configuration, logger);
        if (mode.equals(MODE_LINK)) {
            for (String rootUuid : rootUuids) {
                vcLinker.linkToVcByRootUuid(vcUuid, rootUuid);
                if (Thread.interrupted()) {
                    return TerminationReason.USER_COMMAND;
                }
            }
        } else if (mode.equals(MODE_UNLINK)) {
            for (String rootUuid : rootUuids) {
                vcLinker.unlinkFromVcByRootUuid(vcUuid, rootUuid);
                if (Thread.interrupted()) {
                    return TerminationReason.USER_COMMAND;
                }
            }
        } else throw new IllegalAccessException("Unknown mode \"" + mode + "\"");
        vcLinker.commitAndClose();
        return null;
    }
}
