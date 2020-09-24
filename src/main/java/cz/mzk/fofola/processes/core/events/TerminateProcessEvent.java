package cz.mzk.fofola.processes.core.events;

import cz.mzk.fofola.processes.core.constants.TerminationReason;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class TerminateProcessEvent {

    private final String processId;
    private final TerminationReason reason;
    private final String note;
}
