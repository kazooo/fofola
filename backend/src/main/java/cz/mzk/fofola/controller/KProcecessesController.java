package cz.mzk.fofola.controller;

import cz.mzk.fofola.model.KrameriusProcess;
import cz.mzk.fofola.api.KrameriusApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@AllArgsConstructor
@RequestMapping("/k-processes")
@Slf4j
public class KProcecessesController {

    private static final int processPerPage = 15;
    private final KrameriusApi krameriusApi;

    @GetMapping("/page/{page}")
    @ResponseBody
    public List<KrameriusProcess> getProcessList(@PathVariable int page) {
        int offset = page * processPerPage;
        Map<String, String> filterFields = new HashMap<>() {{
            put("ordering", "DESC");
            put("resultSize", Integer.toString(processPerPage));
            put("offset", Integer.toString(offset));
        }};
        return krameriusApi.getProcesses(filterFields);
    }

    @GetMapping("/{uuid}")
    @ResponseBody
    public KrameriusProcess getProcessInfo(@PathVariable String uuid) {
        return krameriusApi.getProcess(uuid);
    }

    @PutMapping("/{uuid}/stop")
    @ResponseStatus(HttpStatus.OK)
    public void stopKrameriusProcess(@PathVariable String uuid) {
        log.info("Kill process: " + uuid);
        krameriusApi.stopProcess(uuid);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public void removeKrameriusProcess(@PathVariable String uuid) {
        log.info("Remove process: " + uuid);
        krameriusApi.removeProcessLog(uuid);
    }
}
