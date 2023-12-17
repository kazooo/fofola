package cz.mzk.fofola.rest;

import cz.mzk.fofola.model.UuidStateResponse;
import cz.mzk.fofola.model.kprocess.KrameriusProcess;
import cz.mzk.fofola.service.UuidCheckingService;
import cz.mzk.fofola.service.KProcessService;
import cz.mzk.fofola.service.UuidService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class KOperationsController {

    private final UuidCheckingService uuidCheckService;
    private final KProcessService kProcessService;

    @PostMapping("/uuid-info")
    @ResponseStatus(HttpStatus.OK)
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

    @GetMapping("/uuid-info/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public UuidStateResponse checkOnlyOneUuid(@PathVariable("uuid") final String uuid) {
        final String trueUuid = UuidService.makeUuid(uuid);
        log.info("Checking: " + trueUuid);
        return uuidCheckService.checkUuidState(trueUuid);
    }

    @PostMapping("/access/public")
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody final List<String> uuids) {
        for (String uuid : uuids) {
            uuid = UuidService.makeUuid(uuid);
            log.info(String.format("Delete: %s", uuid));
            kProcessService.delete(uuid);
        }
    }
}
