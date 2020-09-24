package cz.mzk.fofola.processes.core.commands;

import cz.mzk.fofola.processes.core.constants.ProcessType;
import cz.mzk.fofola.processes.core.models.Process;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class StartProcessCommand {

    @TargetAggregateIdentifier
    private final String processId;
    private final ProcessType processType;
    private final Process process;
}
