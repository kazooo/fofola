package cz.mzk.fofola.processes.core.commands;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class TerminateProcessCommand {

    @TargetAggregateIdentifier
    private final String processId;
}
