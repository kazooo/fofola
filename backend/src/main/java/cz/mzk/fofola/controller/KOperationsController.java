package cz.mzk.fofola.controller;

import cz.mzk.fofola.model.KrameriusProcess;
import cz.mzk.fofola.model.UuidStateResponse;
import cz.mzk.fofola.service.UuidCheckingService;
import cz.mzk.fofola.service.KProcessService;
import cz.mzk.fofola.service.UuidService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class KOperationsController {

    private final UuidCheckingService uuidCheckService;
    private final KProcessService kProcessService;

    @PostMapping("/uuid-info")
    @ResponseBody
    public List<UuidStateResponse> checkUuidState(@RequestBody List<String> uuids) {
        List<UuidStateResponse> states = new ArrayList<>();
        for (String uuid : uuids) {
            uuid = UuidService.makeUuid(uuid);
            log.info("Checking: " + uuid);
            UuidStateResponse response = uuidCheckService.checkUuidState(uuid);
            states.add(response);
        }
        return states;
    }

    @PostMapping("/access/public")
    @ResponseBody
    private List<KrameriusProcess> makePublic(@RequestBody List<String> uuids) {
        List<KrameriusProcess> krameriusProcesses = new ArrayList<>();
        for (String uuid : uuids) {
            uuid = UuidService.makeUuid(uuid);
            log.info("Make public: " + uuid);
            krameriusProcesses.add(kProcessService.makePublic(uuid));
        }
        return krameriusProcesses;
    }

    @PostMapping("/access/private")
    @ResponseBody
    private List<KrameriusProcess> makePrivate(@RequestBody List<String> uuids) {
        List<KrameriusProcess> krameriusProcesses = new ArrayList<>();
        for (String uuid : uuids) {
            uuid = UuidService.makeUuid(uuid);
            log.info("Make private: " + uuid);
            krameriusProcesses.add(kProcessService.makePrivate(uuid));

        }
        return krameriusProcesses;
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody List<String> uuids) {
        for (String uuid : uuids) {
            uuid = UuidService.makeUuid(uuid);
            log.info("Delete: " + uuid);
            kProcessService.delete(uuid);
        }
    }
}
