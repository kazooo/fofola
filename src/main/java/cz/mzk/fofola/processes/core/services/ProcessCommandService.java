package cz.mzk.fofola.processes.core.services;

import cz.mzk.fofola.processes.core.commands.ActivateProcessCommand;
import cz.mzk.fofola.processes.core.commands.StartProcessCommand;
import cz.mzk.fofola.processes.core.commands.SuspendProcessCommand;
import cz.mzk.fofola.processes.core.commands.TerminateProcessCommand;
import cz.mzk.fofola.processes.core.constants.ProcessType;
import cz.mzk.fofola.processes.core.models.Process;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProcessCommandService {

    private final CommandGateway commandGateway;
    private final EventGateway eventGateway;

    public ProcessCommandService(CommandGateway commandGateway, EventGateway eventGateway) {
        this.commandGateway = commandGateway;
        this.eventGateway = eventGateway;
    }

    public String startNewProcess(ProcessType type, Map<String, Object> params)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String processId = UUID.randomUUID().toString();
        params.put("processId", processId);
        params.put("eventGateway", eventGateway);
        Process process = instantiate(type, params);
        commandGateway.send(new StartProcessCommand(processId, type, process));
        return processId;
    }

    public CompletableFuture<Process> suspendRunningProcess(String processId) {
        return commandGateway.send(new SuspendProcessCommand(processId));
    }

    public CompletableFuture<Process> activateSuspendedProcess(String processId) {
        return commandGateway.send(new ActivateProcessCommand(processId));
    }

    public CompletableFuture<Process> terminateProcess(String processId) {
        return commandGateway.send(new TerminateProcessCommand(processId));
    }

    public static Process instantiate(ProcessType type, Map<String, Object> params)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class processClass = type.getProcessClass();
        if (processClass == null)
            throw new IllegalStateException("No defined process class for type \"" + type.toString() + "\"!");
        Constructor<?> ctor = processClass.getConstructor(params.getClass());
        Object object = ctor.newInstance(params);
        return (Process) object;
    }
}
