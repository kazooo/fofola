package cz.mzk.fofola.process.dnnt;

import cz.mzk.fofola.api.SugoApi;
import cz.mzk.fofola.configuration.ApiConfiguration;
import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.enums.dnnt.DnntLabel;
import cz.mzk.fofola.enums.dnnt.DnntLabelLinkMode;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DnntLabelLinkingProcess extends Process {

    private final List<String> uuids;

    protected final DnntLabel label;
    protected final SugoApi sugoApi;
    protected final Boolean processRecursive;
    private final DnntLabelLinkMode mode;

    @SuppressWarnings("unchecked")
    public DnntLabelLinkingProcess(final ProcessParams params) throws IOException {
        super(params);

        final Map<String, Object> data = params.getData();
        final FofolaConfiguration fofolaConfig = params.getConfig();

        this.uuids = (List<String>) data.get("uuids");

        mode = DnntLabelLinkMode.of((String) data.get("mode"));
        label = DnntLabel.of((String) data.get("label"));
        processRecursive = (Boolean) data.get("processRecursive");
        this.sugoApi = ApiConfiguration.getSugoApi(fofolaConfig);
    }

    @Override
    public TerminationReason process() throws Exception {
        switch (mode) {
            case LINK -> sugoApi.mark(label, processRecursive, uuids);
            case UNLINK -> sugoApi.unmark(label, processRecursive, uuids);
            case SYNC -> sugoApi.sync(uuids);
            case CLEAN -> sugoApi.clean(processRecursive, uuids);
            default -> throw new IllegalArgumentException("Bad mode for DNNT linker: " + mode.getValue() + "!");
        }
        return null;
    }
}
