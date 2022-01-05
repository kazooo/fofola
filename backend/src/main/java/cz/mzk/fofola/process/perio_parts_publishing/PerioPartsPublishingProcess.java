package cz.mzk.fofola.process.perio_parts_publishing;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class PerioPartsPublishingProcess extends Process {

    private final List<String> rootUuids;
    private final FofolaConfiguration configuration;

    @SuppressWarnings("unchecked")
    public PerioPartsPublishingProcess(ProcessParams params) throws IOException {
        super(params);
        final Map<String, ?> data = params.getData();
        configuration = params.getConfig();
        rootUuids = (List<String>) data.get("root_uuids");
    }

    @Override
    public TerminationReason process() throws Exception {
        final PerioPartsPublisher publisher = new PerioPartsPublisher(configuration, 1500, logger);
        for (final String rootUuid : rootUuids) {
            publisher.checkPartsAndMakePublic(rootUuid);
            if (Thread.interrupted()) {
                return TerminationReason.USER_COMMAND;
            }
        }
        publisher.close();
        return null;
    }
}
