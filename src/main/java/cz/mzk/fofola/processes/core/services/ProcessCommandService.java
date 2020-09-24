package cz.mzk.fofola.processes.core.services;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.processes.core.commands.*;
import cz.mzk.fofola.processes.core.constants.ProcessType;
import cz.mzk.fofola.processes.core.models.Process;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
@AllArgsConstructor
public class ProcessCommandService {

    private final FofolaConfiguration fofolaConfiguration;
    private final CommandGateway commandGateway;
    private final EventGateway eventGateway;
    private final ProcessManagementService processManagementService;

    public String startNewProcess(ProcessType type, Map<String, Object> params)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String processId = UUID.randomUUID().toString();
        params.put("processId", processId);
        params.put("eventGateway", eventGateway);
        Process process = instantiate(type, params);
        commandGateway.send(new StartProcessCommand(processId, type, process));
        processManagementService.run(processId, process);
        return processId;
    }

    public CompletableFuture<Process> terminateProcess(String processId) {
        CompletableFuture<Process> result = commandGateway.send(new TerminateProcessCommand(processId));
        processManagementService.terminate(processId);
        return result;
    }

    public Process instantiate(ProcessType type, Map<String, Object> params)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class processClass = type.getProcessClass();
        if (processClass == null)
            throw new IllegalStateException("No defined process class for type \"" + type.toString() + "\"!");
        Constructor<?> ctor = processClass.getConstructor(params.getClass(), FofolaConfiguration.class);
        Object object = ctor.newInstance(params, fofolaConfiguration);
        return (Process) object;
    }

    public CompletableFuture<Process> removeInfoFromDB(String processId) {
        return commandGateway.send(new RemoveInfoCommand(processId));
    }
}
