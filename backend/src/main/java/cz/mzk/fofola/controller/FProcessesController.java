package cz.mzk.fofola.controller;

import cz.mzk.fofola.model.process.ProcessDTO;
import cz.mzk.fofola.service.ProcessManagementService;
import cz.mzk.fofola.model.process.ProcessType;
import cz.mzk.fofola.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/internal-processes")
@AllArgsConstructor
@Slf4j
public class FProcessesController {

    private final ProcessManagementService processCommandService;

    @GetMapping("")
    public String getInternalProcessManagementPage() {
        return "internal-processes";
    }

    @PostMapping("/new/{processTypeAlias}")
    @ResponseBody
    public String startNewProcess(@PathVariable String processTypeAlias,
                                  @RequestBody Map<String, Object> params,
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
        return processCommandService.start(type, params);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<ProcessDTO> getAllProcesses() {
        return processCommandService.findAllProcess();
    }

    @GetMapping("/logs/{logFileName}")
    @ResponseBody
    public String getLogFileContent(@PathVariable String logFileName) throws IOException {
        String filePath = FileService.logDirPath + logFileName;
        return String.join("<br/>", Files.readAllLines(Paths.get(filePath)));
    }

    @PutMapping("/stop/{pid}")
    @ResponseBody
    public String stopRunningProcess(@PathVariable String pid) {
        log.info("Got terminate process command for pid: " + pid);
        processCommandService.terminate(pid);
        return pid;
    }

    @DeleteMapping("/remove/{pid}")
    @ResponseBody
    public String removeProcessFromDB(@PathVariable String pid) {
        log.info("Got remove process command for pid: " + pid);
        processCommandService.removeInfoFromDB(pid);
        return pid;
    }
}
