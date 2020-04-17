package cz.mzk.fofola.controller;

import cz.mzk.fofola.processes.core.constants.ProcessType;
import cz.mzk.fofola.processes.core.models.ProcessDTO;
import cz.mzk.fofola.processes.core.services.ProcessCommandService;
import cz.mzk.fofola.processes.core.services.ProcessQueryService;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/processes")
public class ProcessController {

    private final ProcessCommandService processCommandService;
    private final ProcessQueryService processQueryService;

    public ProcessController(ProcessCommandService processCommandService,
                             ProcessQueryService processQueryService) {
        this.processCommandService = processCommandService;
        this.processQueryService = processQueryService;
    }

    @GetMapping("/new/{processTypeAlias}")
    @ResponseBody
    public String startNewProcess(@PathVariable String processTypeAlias)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ProcessType type = ProcessType.findByAlias(processTypeAlias);
        if (type == null)
            throw new IllegalStateException("Can't determine process for alias \"" + processTypeAlias + "\"!");
        return processCommandService.startNewProcess(type, new HashMap<>());
    }

    @GetMapping("/all")
    public CompletableFuture<List<ProcessDTO>> getAllProcesses() {
        return processQueryService.findAllProcess();
    }

    @GetMapping("/suspend/{processId}")
    @ResponseBody
    public String suspendRunningProcess(@PathVariable String processId) {
        processCommandService.suspendRunningProcess(processId);
        return processId;
    }

    @GetMapping("/activate/{processId}")
    @ResponseBody
    public String activateRunningProcess(@PathVariable String processId) {
        processCommandService.activateSuspendedProcess(processId);
        return processId;
    }

    @GetMapping("/terminate/{processId}")
    @ResponseBody
    public String terminateRunningProcess(@PathVariable String processId) {
        processCommandService.terminateProcess(processId);
        return processId;
    }
}
