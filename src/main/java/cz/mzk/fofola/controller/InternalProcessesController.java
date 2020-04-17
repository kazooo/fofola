package cz.mzk.fofola.controller;

import cz.mzk.fofola.processes.core.constants.ProcessType;
import cz.mzk.fofola.processes.core.models.ProcessDTO;
import cz.mzk.fofola.processes.core.services.ProcessCommandService;
import cz.mzk.fofola.processes.core.services.ProcessQueryService;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@RestController
@RequestMapping("/processes")
public class InternalProcessesController {

    private final ProcessCommandService processCommandService;
    private final ProcessQueryService processQueryService;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public InternalProcessesController(ProcessCommandService processCommandService,
                                       ProcessQueryService processQueryService) {
        this.processCommandService = processCommandService;
        this.processQueryService = processQueryService;
    }

    @PostMapping("/new/{processTypeAlias}")
    @ResponseBody
    public String startNewProcess(@PathVariable String processTypeAlias, @RequestBody Map<String, Object> params)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ProcessType type = ProcessType.findByAlias(processTypeAlias);
        if (type == null)
            throw new IllegalStateException("Can't determine process for alias \"" + processTypeAlias + "\"!");
        return processCommandService.startNewProcess(type, params);
    }

    @GetMapping("/all")
    public CompletableFuture<List<ProcessDTO>> getAllProcesses() {
        return processQueryService.findAllProcess();
    }

    @PutMapping("/suspend/{processId}")
    @ResponseBody
    public String suspendRunningProcess(@PathVariable String processId) {
        processCommandService.suspendRunningProcess(processId);
        return processId;
    }

    @PutMapping("/activate/{processId}")
    @ResponseBody
    public String activateRunningProcess(@PathVariable String processId) {
        processCommandService.activateSuspendedProcess(processId);
        return processId;
    }

    @PutMapping("/terminate/{processId}")
    @ResponseBody
    public String terminateRunningProcess(@PathVariable String processId) {
        processCommandService.terminateProcess(processId);
        return processId;
    }
}
