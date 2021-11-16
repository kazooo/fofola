package cz.mzk.fofola.process.dnnt;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.enums.dnnt.DnntLabelEnum;
import cz.mzk.fofola.enums.dnnt.DnntLabelLinkModeEnum;
import cz.mzk.fofola.model.process.Process;
import cz.mzk.fofola.model.process.ProcessParams;
import cz.mzk.fofola.model.process.TerminationReason;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DnntLabelLinkingProcess extends Process {

    private final DnntLabelLinkingRunner runner;
    private final List<String> uuids;

    public DnntLabelLinkingProcess(final ProcessParams params)
            throws IOException, TransformerConfigurationException, XPathExpressionException, ParserConfigurationException {
        super(params);

        final Map<String, ?> data = params.getData();
        final FofolaConfiguration fofolaConfig = params.getConfig();

        final List<String> uuids = (List) data.get("uuids");
        final DnntLabelLinkModeEnum linkMode = DnntLabelLinkModeEnum.of((String) data.get("mode"));

        final DnntLinkerParams linkerParams = new DnntLinkerParams(data, fofolaConfig, logger, 1500);

        this.uuids = uuids;
        if (linkMode == DnntLabelLinkModeEnum.LINK) {
            runner = new DnntLabelLinkRunnerImpl(linkerParams);
        } else {
            runner = new DnntLabelUnlinkRunnerImpl(linkerParams);
        }
    }

    @Override
    public TerminationReason process() throws Exception {
        for (String uuid : uuids) {
            runner.run(uuid);
            if (Thread.interrupted()) {
                return TerminationReason.USER_COMMAND;
            }
        }
        runner.commitAndClose();
        return null;
    }
}