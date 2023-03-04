package cz.mzk.fofola.process.dnnt;

import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.constants.dnnt.*;
import cz.mzk.fofola.model.dnnt.SugoMarkParams;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DnntLabelLinkingProcess extends Process {

    private final List<String> uuids;

    protected final Label label;
    protected final SugoApi sugoApi;
    protected final Boolean processRecursive;
    private final MarkMode mode;

    @SuppressWarnings("unchecked")
    public DnntLabelLinkingProcess(final ProcessParams params) throws IOException {
        super(params);

        final Map<String, Object> data = params.getData();
        final FofolaConfiguration fofolaConfig = params.getConfig();

        this.uuids = (List<String>) data.get("uuids");

        mode = MarkMode.of((String) data.get("mode"));
        label = BasicLabel.of((String) data.get("label"));
        processRecursive = (Boolean) data.get("processRecursive");
        this.sugoApi = ApiConfiguration.getSugoApi(fofolaConfig);
    }

    @Override
    public TerminationReason process() throws Exception {
        logger.info(String.format(
                "Label %s, mode %s, processRecursive %s, documents %d",
                label.getValue(), mode.getValue(), processRecursive.toString(), uuids.size()
        ));
        uuids.forEach(logger::info);

        final SugoMarkParams params = SugoMarkParams.builder()
                .direction(Direction.SELECTED_IN_KRAMERIUS)
                .requestor(Requestor.REST)
                .operation(mode.getOperation())
                .uuids(uuids)
                .label(label)
                .recursively(processRecursive)
                .build();

        final Long sugoSessionId;
        switch (mode) {
            case LINK -> sugoSessionId = sugoApi.mark(params);
            case UNLINK -> sugoSessionId = sugoApi.unmark(params);
            case SYNC -> sugoSessionId = sugoApi.sync(params);
            case CLEAN -> sugoSessionId = sugoApi.clean(params);
            default -> throw new IllegalArgumentException("Bad mode for DNNT linker: " + mode.getValue() + "!");
        }

        logger.info("Sugo session id is " + sugoSessionId);

        return null;
    }
}
