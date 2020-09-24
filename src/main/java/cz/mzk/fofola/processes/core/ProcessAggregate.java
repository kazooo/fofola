package cz.mzk.fofola.processes.core;

import cz.mzk.fofola.processes.core.commands.*;
import cz.mzk.fofola.processes.core.constants.TerminationReason;
import cz.mzk.fofola.processes.core.events.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;


@Aggregate
public class ProcessAggregate {

    @AggregateIdentifier
    private String processId;

    @CommandHandler
    public ProcessAggregate(StartProcessCommand command) {
        processId = command.getProcessId();
        apply(new StartProcessEvent(command.getProcessId(), command.getProcessType()));
    }

    @CommandHandler
    public void handle(TerminateProcessCommand command) {
        apply(new TerminateProcessEvent(command.getProcessId(), TerminationReason.USER_COMMAND, null));
    }

    @CommandHandler
    public void handle(RemoveInfoCommand command) {
        apply(new RemoveInfoEvent(command.getProcessId()));
    }

    @EventSourcingHandler
    public void on(StartProcessEvent event) {
        processId = event.getProcessId();
    }

    @EventSourcingHandler
    public void on(TerminateProcessEvent event) {
        processId = event.getProcessId();
    }

    @EventSourcingHandler
    public void on(RemoveInfoEvent event) {
        processId = event.getProcessId();
    }
    protected ProcessAggregate() {
        // Required by Axon to build a default Aggregate prior to Event Sourcing
    }
}
