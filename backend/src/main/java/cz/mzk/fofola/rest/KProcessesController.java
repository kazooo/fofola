package cz.mzk.fofola.rest;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.kprocess.KrameriusProcess;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/k-processes")
@Slf4j
public class KProcessesController {

    private static final int processPerPage = 15;
    private final KrameriusApi krameriusApi;

    @GetMapping("/page/{page}")
    @ResponseBody
    public List<KrameriusProcess> getProcessList(@PathVariable int page) {
        int offset = page * processPerPage;
        MultiValueMap<String, String> filterFields = new LinkedMultiValueMap<>() {{
            add("ordering", "DESC");
            add("resultSize", Integer.toString(processPerPage));
            add("offset", Integer.toString(offset));
        }};
        return krameriusApi.getProcesses(filterFields);
    }

    @GetMapping("/{uuid}")
    @ResponseBody
    public KrameriusProcess getProcessInfo(@PathVariable String uuid) {
        return krameriusApi.getProcess(uuid);
    }

    @PutMapping("/{processId}/stop")
    @ResponseStatus(HttpStatus.OK)
    public void stopKrameriusProcess(@PathVariable String processId) {
        log.info("Kill process: " + processId);
        krameriusApi.stopProcess(processId);
    }

    @DeleteMapping("/{processId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeKrameriusProcess(@PathVariable String processId) {
        log.info("Remove process: " + processId);
        krameriusApi.removeProcess(processId);
    }
}
