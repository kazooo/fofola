package cz.mzk.fofola.process.vc_linker;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;
import cz.mzk.fofola.service.UuidService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class VCLinkerProcess extends Process {

    private final String vcId;
    private final List<String> rootUuids;
    private final KrameriusApi krameriusApi;

    private final String mode;
    private static final String MODE_LINK = "LINK_MODE";
    private static final String MODE_UNLINK = "UNLINK_MODE";

    @SuppressWarnings("unchecked")
    public VCLinkerProcess(ProcessParams params) throws IOException {
        super(params);
        krameriusApi = params.getKrameriusApi();
        Map<String, ?> data = params.getData();
        mode = (String) data.get("mode");
        vcId = (String) data.get("vcUuid");
        rootUuids = (List<String>) data.get("uuids");
    }

    @Override
    public TerminationReason process() throws Exception {
        if (mode.equals(MODE_LINK)) {
            for (String rootUuid : rootUuids) {
                try {
                    krameriusApi.addToVc(vcId, UuidService.makeUuid(rootUuid));
                } catch (Exception e) {
                    logger.warning(e.getMessage());
                }

                if (Thread.interrupted()) {
                    return TerminationReason.USER_COMMAND;
                }
            }
        } else if (mode.equals(MODE_UNLINK)) {
            for (String rootUuid : rootUuids) {
                try {
                    krameriusApi.removeFromVc(vcId, UuidService.makeUuid(rootUuid));
                } catch (Exception e) {
                    logger.warning(e.getMessage());
                }

                if (Thread.interrupted()) {
                    return TerminationReason.USER_COMMAND;
                }
            }
        } else throw new IllegalAccessException("Unknown mode \"" + mode + "\"");
        return null;
    }
}
