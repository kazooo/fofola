package cz.mzk.fofola.controller;

import com.google.gson.Gson;
import cz.mzk.fofola.kramerius_api.Process;
import cz.mzk.fofola.model.KrameriusDocument;
import cz.mzk.fofola.researcher.UuidResearcher;
import cz.mzk.fofola.service.IpLogger;
import cz.mzk.fofola.service.KrameriusApiCommunicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;


@Controller
@Slf4j
public class KrameriusOperationsController {

    private static final Gson gson = new Gson();

    private final UuidResearcher researcher;
    private final KrameriusApiCommunicator krameriusApi;

    public KrameriusOperationsController(UuidResearcher researcher,
                          KrameriusApiCommunicator krameriusApi) {
        this.researcher = researcher;
        this.krameriusApi = krameriusApi;
    }

    @GetMapping("/check_uuid")
    public String getCheckUuidPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry uuid checking section.");
        return "check_uuid";
    }

    @MessageMapping("/check-websocket")
    @SendTo("/check/data")
    public String getTreeDataWebSocket(String uuid, SimpMessageHeaderAccessor ha) {
        Object ipAdress = ha.getSessionAttributes().get("IP");
        IpLogger.logIp(ipAdress.toString(), "Checking: " + uuid);
        KrameriusDocument krameriusDoc = fillKrameriusDoc(new KrameriusDocument(uuid));
        IpLogger.logIp(ipAdress.toString(), "Finish checking: " + uuid);
        return gson.toJson(krameriusDoc);
    }

    @GetMapping("/change_rights")
    public String getChangeVisibilityPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry uuid checking section.");
        return "change_rights";
    }

    @MessageMapping("/rights-websocket")
    @SendTo("/processes/rights")
    public Process changeRight(@Payload String uuid, @Header("action") String action,
                            SimpMessageHeaderAccessor ha) throws Exception {
        Object ipAdress = ha.getSessionAttributes().get("IP");
        Process p = null;
        switch (action) {
            case "public":
                IpLogger.logIp(ipAdress.toString(), "Make public: " + uuid);
                p = krameriusApi.makePublic(uuid);
                break;
            case "private":
                IpLogger.logIp(ipAdress.toString(), "Make private: " + uuid);
                p = krameriusApi.makePrivate(uuid);
                break;
        }
        return p;  // return process info to display progress on page
    }

    @GetMapping("/reindex")
    public String getReindexPage(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry reindexation section.");
        return "reindex";
    }

    @MessageMapping("/reindex-websocket")
    public void loadDataToReindex(@Payload String uuid, SimpMessageHeaderAccessor ha) throws Exception {
        Object ipAdress = ha.getSessionAttributes().get("IP");
        IpLogger.logIp(ipAdress.toString(), "Reindex: " + uuid);
        krameriusApi.reindex(uuid);
    }

    private KrameriusDocument fillKrameriusDoc(KrameriusDocument krameriusDoc) {
        krameriusDoc = researcher.fillKrameriusDoc(krameriusDoc);
        return krameriusDoc;
    }
}
