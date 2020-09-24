package cz.mzk.fofola.processes.core.commands;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class RemoveInfoCommand {

    @TargetAggregateIdentifier
    private final String processId;
}
