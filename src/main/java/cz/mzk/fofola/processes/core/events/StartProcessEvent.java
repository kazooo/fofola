package cz.mzk.fofola.processes.core.events;

import cz.mzk.fofola.processes.core.constants.ProcessType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class StartProcessEvent {

    private final String processId;
    private final ProcessType processType;
}
