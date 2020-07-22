package cz.mzk.fofola.controller;

import cz.mzk.fofola.processes.core.constants.ProcessType;
import cz.mzk.fofola.processes.core.models.ProcessDTO;
import cz.mzk.fofola.processes.core.services.ProcessCommandService;
import cz.mzk.fofola.processes.core.services.ProcessQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/internal-processes")
@Slf4j
public class InternalProcessManagementController {

    private final ProcessCommandService processCommandService;
    private final ProcessQueryService processQueryService;

    public InternalProcessManagementController(ProcessCommandService processCommandService,
                                               ProcessQueryService processQueryService) {
        this.processCommandService = processCommandService;
        this.processQueryService = processQueryService;
    }

    @GetMapping("")
    public String getInternalProcessManagementPage() {
        return "internal_processes";
    }

    @PostMapping("/new/{processTypeAlias}")
    @ResponseBody
    public String startNewProcess(@PathVariable String processTypeAlias,
                                  @RequestPart(value = "params") Map<String, Object> params,
                                  @RequestPart(value = "image", required = false) MultipartFile image)
            throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        ProcessType type = ProcessType.findByAlias(processTypeAlias);
        if (type == null)
            throw new IllegalStateException(
                    "Can't determine process for alias \"" + processTypeAlias + "\"!"
            );
        if (image != null) {
            params.put("image", image);
        }
        return processCommandService.startNewProcess(type, params);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<ProcessDTO> getAllProcesses() {
        return processQueryService.findAllProcess();
    }

    @PutMapping("/terminate/{processId}")
    @ResponseBody
    public String terminateRunningProcess(@PathVariable String processId) {
        log.info("Got terminate process command for pid: " + processId);
        processCommandService.terminateProcess(processId);
        return processId;
    }

    @DeleteMapping("/remove/{processId}")
    @ResponseBody
    public String removeProcessFromDB(@PathVariable String processId) {
        log.info("Got remove process command for pid: " + processId);
        processCommandService.removeInfoFromDB(processId);
        return processId;
    }
}
