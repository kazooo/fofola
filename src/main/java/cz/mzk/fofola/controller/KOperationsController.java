package cz.mzk.fofola.controller;

import com.google.gson.Gson;
import cz.mzk.fofola.model.KrameriusProcess;
import cz.mzk.fofola.model.UuidStateResponse;
import cz.mzk.fofola.service.UuidCheckingService;
import cz.mzk.fofola.service.KProcessService;
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
    private final KProcessService kProcessService;

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

    @PostMapping("/change-access")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public List<KrameriusProcess> changeAccessibility
            (@RequestPart(value = "params") Map<String, Object> params) {
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

    private List<KrameriusProcess> makePublic(List<String> uuids) {
        List<KrameriusProcess> krameriusProcesses = new ArrayList<>();
        for (String uuid : uuids) {
            log.info("Make public: " + uuid);
            krameriusProcesses.add(kProcessService.makePublic(uuid));

        }
        return krameriusProcesses;
    }

    private List<KrameriusProcess> makePrivate(List<String> uuids) {
        List<KrameriusProcess> krameriusProcesses = new ArrayList<>();
        for (String uuid : uuids) {
            log.info("Make public: " + uuid);
            krameriusProcesses.add(kProcessService.makePrivate(uuid));

        }
        return krameriusProcesses;
    }

    @PostMapping("/reindex")
    public void reindex(@RequestPart(value = "uuids") List<String> uuids) {
        for (String uuid : uuids) {
            log.info("Reindex: " + uuid);
            kProcessService.reindex(uuid);
        }
    }
}
