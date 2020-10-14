package cz.mzk.fofola.controller;

import com.google.gson.Gson;
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

    private static final Gson gson = new Gson();
    private static final int processPerPage = 15;
    private final KrameriusApi krameriusApi;

    @GetMapping("/page/{page}")
    @ResponseBody
    public String getProcessList(@PathVariable int page) {
        int offset = page * processPerPage;
        Map<String, String> filterFields = new HashMap<String, String>() {{
            put("ordering", "DESC");
            put("resultSize", Integer.toString(processPerPage));
            put("offset", Integer.toString(offset));
        }};
        List<KrameriusProcess> krameriusProcessList = krameriusApi.getProcesses(filterFields);
        return gson.toJson(krameriusProcessList);
    }

    @GetMapping("/{uuid}")
    @ResponseBody
    public String getProcessInfo(@PathVariable String uuid) {
        return gson.toJson(krameriusApi.getProcess(uuid));
    }

    @PostMapping("/command")
    @ResponseStatus(HttpStatus.OK)
    public void receiveCommandForKrameriusProcess(@RequestPart(value = "params") Map<String, Object> params) {
        String action = (String) params.get("action");
        String pid = (String) params.get("pid");
        switch (action) {
            case "kill":
                log.info("Kill process: " + pid);
                krameriusApi.stopProcess(pid);
                break;
            case "remove":
                log.info("Remove process: " + pid);
                krameriusApi.removeProcessLog(pid);
                break;
        }
    }
}
