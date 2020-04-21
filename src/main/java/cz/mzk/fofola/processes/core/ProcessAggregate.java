package cz.mzk.fofola.processes.core;

import cz.mzk.fofola.processes.core.commands.*;
import cz.mzk.fofola.processes.core.constants.FinishReason;
import cz.mzk.fofola.processes.core.events.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.logging.Logger;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ProcessAggregate {

    private static final Logger logger = Logger.getLogger(ProcessAggregate.class.getName());

    @AggregateIdentifier
    private String processId;

    @CommandHandler
    public ProcessAggregate(StartProcessCommand command) {
        processId = command.getProcessId();
        apply(new StartProcessEvent(command.getProcessId(), command.getProcessType()));
    }

    @CommandHandler
    public void handle(SuspendProcessCommand command) {
        apply(new SuspendProcessEvent(command.getProcessId()));
    }

    @CommandHandler
    public void handle(ActivateProcessCommand command) {
        apply(new ActivateProcessEvent(command.getProcessId()));
    }

    @CommandHandler
    public void handle(TerminateProcessCommand command) {
        apply(new TerminateProcessEvent(command.getProcessId(), FinishReason.USER_COMMAND, null));
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
    public void on(SuspendProcessEvent event) {
        processId = event.getProcessId();
    }

    @EventSourcingHandler
    public void on(ActivateProcessEvent event) {
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
