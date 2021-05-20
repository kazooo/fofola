package cz.mzk.fofola.model.process;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.process.ProcessEventNotifier;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class ProcessParams {

    private String id;
    private ProcessType type;
    private Map<String, ?> data;
    private FofolaConfiguration config;
    private ProcessEventNotifier eventNotifier;
}
