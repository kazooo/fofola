package cz.mzk.fofola.controller;

import com.google.gson.Gson;
import cz.mzk.fofola.kramerius_api.Process;
import cz.mzk.fofola.model.UuidStateResponse;
import cz.mzk.fofola.service.UuidCheckingService;
import cz.mzk.fofola.service.KrameriusApiCommunicator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Controller
@AllArgsConstructor
@Slf4j
public class KOperationsController {

    private static final Gson gson = new Gson();
    private final UuidCheckingService uuidCheckService;
    private final KrameriusApiCommunicator krameriusApi;

    @GetMapping("/check-uuid")
    public String getCheckUuidPage() {
        log.info("Entry uuid checking section.");
        return "check-uuid";
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

    @GetMapping("/change-access")
    public String getChangeVisibilityPage() {
        log.info("Entry uuid checking section.");
        return "change-access";
    }

    @PostMapping("/change-access")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public List<Process> changeAccessibility
            (@RequestPart(value = "params") Map<String, Object> params) throws Exception {
        String accessibility = (String) params.get("access");
        List<String> uuids = (List<String>) params.get("uuids");
        switch (accessibility) {
            case "public":
                return makePublic(uuids);
            case "private":
                return makePrivate(uuids);
        }
        return Collections.emptyList();
    }

    private List<Process> makePublic(List<String> uuids) throws Exception {
        List<Process> processes = new ArrayList<>();
        for (String uuid : uuids) {
            log.info("Make public: " + uuid);
            processes.add(krameriusApi.makePublic(uuid));

        }
        return processes;
    }

    private List<Process> makePrivate(List<String> uuids) throws Exception {
        List<Process> processes = new ArrayList<>();
        for (String uuid : uuids) {
            log.info("Make public: " + uuid);
            processes.add(krameriusApi.makePrivate(uuid));

        }
        return processes;
    }

    @GetMapping("/reindex")
    public String getReindexPage() {
        log.info("Entry reindexation section.");
        return "reindex";
    }

    @PostMapping("/reindex")
    public void reindex(@RequestPart(value = "uuids") List<String> uuids) throws Exception {
        for (String uuid : uuids) {
            log.info("Reindex: " + uuid);
            krameriusApi.reindex(uuid);
        }
    }
}
