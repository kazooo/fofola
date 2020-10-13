package cz.mzk.fofola.controller;

import com.google.gson.Gson;
import cz.mzk.fofola.service.KrameriusApiCommunicator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@AllArgsConstructor
@RequestMapping("/k-processes")
@Slf4j
public class KProcecessesController {

    private static final Gson gson = new Gson();
    private static final int processPerPage = 15;
    private final KrameriusApiCommunicator krameriusApi;

    @GetMapping("")
    public String getProcessControlPage() {
        log.info("Entry process control section.");
        return "k-processes";
    }

    @GetMapping("/page/{page}")
    @ResponseBody
    public String getProcessList(@PathVariable int page) throws Exception {
        return gson.toJson(krameriusApi.getProcessList(page * processPerPage, processPerPage));
    }

    @GetMapping("/{uuid}")
    @ResponseBody
    public String getProcessInfo(@PathVariable String uuid) throws Exception {
        return gson.toJson(krameriusApi.getProcessInfo(uuid));
    }

    @PostMapping("/command")
    @ResponseStatus(HttpStatus.OK)
    public void receiveCommandForKrameriusProcess(@RequestPart(value = "params") Map<String, Object> params) throws Exception {
        String action = (String) params.get("action");
        String pid = (String) params.get("pid");
        switch (action) {
            case "kill":
                log.info("Kill process: " + pid);
                krameriusApi.stopProcess(pid);
                break;
            case "remove":
                log.info("Remove process: " + pid);
                krameriusApi.removeProcess(pid);
                break;
        }
    }
}
