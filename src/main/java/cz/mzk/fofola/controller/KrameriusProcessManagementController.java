package cz.mzk.fofola.controller;

import com.google.gson.Gson;
import cz.mzk.fofola.kramerius_api.Process;
import cz.mzk.fofola.service.KrameriusApiCommunicator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
@AllArgsConstructor
@Slf4j
public class KrameriusProcessManagementController {

    private static final Gson gson = new Gson();
    private static final int processPerPage = 15;
    private final KrameriusApiCommunicator krameriusApi;

    @GetMapping("/kramerius-processes")
    public String getProcessControlPage() {
        log.info("Entry process control section.");
        return "processes";
    }

    @MessageMapping("/process-websocket")
    @SendTo("/processes/info")
    public String getProcessList(@Payload int pageNum) throws Exception {
        List<Process> processList = krameriusApi.getProcessList(pageNum * processPerPage, processPerPage);
        return gson.toJson(processList);
    }

    @GetMapping("/k-processes/{uuid}")
    @ResponseBody
    public String getProcessInfo(@PathVariable String uuid) throws Exception {
        return gson.toJson(krameriusApi.getProcessInfo(uuid));
    }

    @PostMapping("/k-processes/command")
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
