package cz.mzk.fofola.process.dnnt;

import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.constants.dnnt.BasicLabel;
import cz.mzk.fofola.constants.dnnt.Label;
import cz.mzk.fofola.constants.dnnt.MarkMode;
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
        final SugoMarkParams params = SugoMarkParams.builder()
                .label(label)
                .recursively(processRecursive)
                .build();

        switch (mode) {
            case LINK -> sugoApi.mark(params, uuids);
            case UNLINK -> sugoApi.unmark(params, uuids);
            case SYNC -> sugoApi.sync(uuids);
            case CLEAN -> sugoApi.clean(params, uuids);
            default -> throw new IllegalArgumentException("Bad mode for DNNT linker: " + mode.getValue() + "!");
        }

        return null;
    }
}
