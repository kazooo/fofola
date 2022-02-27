package cz.mzk.fofola.rest;

import cz.mzk.fofola.service.KProcessService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class ReindexController {

    private final KProcessService kProcessService;

    @PostMapping("/reindex")
    @ResponseStatus(HttpStatus.OK)
    public void reindex(@RequestBody List<String> uuids) {
        for (String uuid : uuids) {
            log.info("Reindex: " + uuid);
            kProcessService.reindex(uuid);
        }
    }
}
