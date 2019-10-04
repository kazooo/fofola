package cz.mzk.integrity.controller;

import com.google.gson.Gson;
import cz.mzk.integrity.kramerius_api.Process;
import cz.mzk.integrity.service.IpLogger;
import cz.mzk.integrity.service.KrameriusApiCommunicator;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class ProcessControlController {

    private static final Gson gson = new Gson();
    private static final int processPerPage = 16;
    private final KrameriusApiCommunicator krameriusApi;
    private static final Logger logger = Logger.getLogger(ProcessControlController.class.getName());

    public ProcessControlController(KrameriusApiCommunicator krameriusApi) {
        this.krameriusApi = krameriusApi;
    }

    @GetMapping("/processes")
    public String getProcessControlPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry process control section.");
        return "processes";
    }

    @MessageMapping("/process-websocket")
    @SendTo("/processes/info")
    public String getTreeDataWebSocket(@Payload int pageNum) throws Exception {
        List<Process> processList = krameriusApi.getProcessList(pageNum * processPerPage, processPerPage);
        return gson.toJson(processList);
    }
}
