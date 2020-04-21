package cz.mzk.fofola.controller;

import com.google.gson.Gson;
import cz.mzk.fofola.kramerius_api.Process;
import cz.mzk.fofola.service.IpLogger;
import cz.mzk.fofola.service.KrameriusApiCommunicator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class KrameriusProcessManagementController {

    private static final Gson gson = new Gson();
    private static final int processPerPage = 15;
    private final KrameriusApiCommunicator krameriusApi;
    private static final Logger logger = Logger.getLogger(KrameriusProcessManagementController.class.getName());

    public KrameriusProcessManagementController(KrameriusApiCommunicator krameriusApi) {
        this.krameriusApi = krameriusApi;
    }

    @GetMapping("/kramerius-processes")
    public String getProcessControlPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry process control section.");
        return "processes";
    }

    @MessageMapping("/process-websocket")
    @SendTo("/processes/info")
    public String getProcessList(@Payload int pageNum) throws Exception {
        List<Process> processList = krameriusApi.getProcessList(pageNum * processPerPage, processPerPage);
        return gson.toJson(processList);
    }

    @MessageMapping("/process-info")
    @SendTo("/processes/one-p-info")
    public String getProcessInfo(@Payload String processUuid) throws Exception {
        return gson.toJson(krameriusApi.getProcessInfo(processUuid));
    }

    @MessageMapping("/process-manipulation-websocket")
    public void applyOperation(@Payload String processUuid, @Header("action") String action,
                               SimpMessageHeaderAccessor ha) throws Exception {
        Object ipAdress = ha.getSessionAttributes().get("IP");
        switch (action) {
            case "kill":
                IpLogger.logIp(ipAdress.toString(), "Kill process: " + processUuid);
                krameriusApi.stopProcess(processUuid);
                break;
            case "remove":
                IpLogger.logIp(ipAdress.toString(), "Remove process: " + processUuid);
                krameriusApi.removeProcess(processUuid);
                break;
        }
    }
}
