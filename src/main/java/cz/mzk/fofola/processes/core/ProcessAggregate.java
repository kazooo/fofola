package cz.mzk.fofola.processes.core;

import cz.mzk.fofola.processes.core.commands.ActivateProcessCommand;
import cz.mzk.fofola.processes.core.commands.StartProcessCommand;
import cz.mzk.fofola.processes.core.commands.SuspendProcessCommand;
import cz.mzk.fofola.processes.core.commands.TerminateProcessCommand;
import cz.mzk.fofola.processes.core.constants.FinishReason;
import cz.mzk.fofola.processes.core.events.ActivateProcessEvent;
import cz.mzk.fofola.processes.core.events.StartProcessEvent;
import cz.mzk.fofola.processes.core.events.SuspendProcessEvent;
import cz.mzk.fofola.processes.core.events.TerminateProcessEvent;
import cz.mzk.fofola.processes.core.services.ProcessManagementService;
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
    public ProcessAggregate(StartProcessCommand command, ProcessManagementService processManagementService) {
        processId = command.getProcessId();
        apply(new StartProcessEvent(command.getProcessId(), command.getProcessType()));
        processManagementService.run(command.getProcessId(), command.getProcess());
    }

    @CommandHandler
    public void handle(SuspendProcessCommand command, ProcessManagementService processManagementService) {
        apply(new SuspendProcessEvent(command.getProcessId()));
        processManagementService.suspend(command.getProcessId());
    }

    @CommandHandler
    public void handle(ActivateProcessCommand command, ProcessManagementService processManagementService) {
        apply(new ActivateProcessEvent(command.getProcessId()));
        processManagementService.activate(command.getProcessId());
    }

    @CommandHandler
    public void handle(TerminateProcessCommand command, ProcessManagementService processManagementService) {
        apply(new TerminateProcessEvent(command.getProcessId(), FinishReason.USER_COMMAND, null));
        processManagementService.terminate(command.getProcessId());
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

    protected ProcessAggregate() {
        // Required by Axon to build a default Aggregate prior to Event Sourcing
    }
}
