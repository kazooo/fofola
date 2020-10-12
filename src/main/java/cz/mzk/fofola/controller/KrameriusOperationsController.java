package cz.mzk.fofola.controller;

import com.google.gson.Gson;
import cz.mzk.fofola.kramerius_api.Process;
import cz.mzk.fofola.model.UuidStateResponse;
import cz.mzk.fofola.service.UuidCheckingService;
import cz.mzk.fofola.service.KrameriusApiCommunicator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@AllArgsConstructor
@Slf4j
public class KrameriusOperationsController {

    private static final Gson gson = new Gson();
    private final UuidCheckingService uuidCheckService;
    private final KrameriusApiCommunicator krameriusApi;

    @GetMapping("/check-uuid")
    public String getCheckUuidPage() {
        log.info("Entry uuid checking section.");
        return "check_uuid";
    }

    @PostMapping("/check-uuid")
    @ResponseBody
    public String checkUuidState(@RequestPart(value = "uuids") List<String> uuids) {
        List<UuidStateResponse> states = new ArrayList<>();
        for (String uuid : uuids) {
            log.info("Checking: " + uuid);
            UuidStateResponse response = uuidCheckService.checkUuidState(uuid);
            states.add(response);
        }
        return gson.toJson(states);
    }

    @GetMapping("/change_rights")
    public String getChangeVisibilityPage() {
        log.info("Entry uuid checking section.");
        return "change_rights";
    }

    @MessageMapping("/rights-websocket")
    @SendTo("/processes/rights")
    public Process changeRight(@Payload String uuid, @Header("action") String action) throws Exception {
        Process p = null;
        switch (action) {
            case "public":
                log.info("Make public: " + uuid);
                p = krameriusApi.makePublic(uuid);
                break;
            case "private":
                log.info("Make private: " + uuid);
                p = krameriusApi.makePrivate(uuid);
                break;
        }
        return p;  // return process info to display progress on page
    }

    @GetMapping("/reindex")
    public String getReindexPage() {
        log.info("Entry reindexation section.");
        return "reindex";
    }

    @MessageMapping("/reindex-websocket")
    public void loadDataToReindex(@Payload String uuid) throws Exception {
        log.info("Reindex: " + uuid);
        krameriusApi.reindex(uuid);
    }
}
