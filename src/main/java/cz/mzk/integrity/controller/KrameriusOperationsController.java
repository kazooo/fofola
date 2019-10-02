package cz.mzk.integrity.controller;

import com.google.gson.Gson;
import cz.mzk.integrity.model.KrameriusDocListWrapper;
import cz.mzk.integrity.model.KrameriusDocument;
import cz.mzk.integrity.researcher.UuidResearcher;
import cz.mzk.integrity.service.IpLogger;
import cz.mzk.integrity.service.KrameriusApiCommunicator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Controller
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
    public void changeRight(@Payload String uuid, @Header("action") String action,
                            SimpMessageHeaderAccessor ha) throws Exception {
        Object ipAdress = ha.getSessionAttributes().get("IP");
        switch (action) {
            case "public":
                IpLogger.logIp(ipAdress.toString(), "Make public: " + uuid);
                krameriusApi.makePublic(uuid);
                break;
            case "private":
                IpLogger.logIp(ipAdress.toString(), "Make private: " + uuid);
                krameriusApi.makePrivate(uuid);
                break;
        }
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
