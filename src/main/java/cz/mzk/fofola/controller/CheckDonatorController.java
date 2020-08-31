package cz.mzk.fofola.controller;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.vc.VC;
import cz.mzk.fofola.processes.utils.FileUtils;
import cz.mzk.fofola.service.IpLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("/check-donator")
@Slf4j
public class CheckDonatorController {

    private final FofolaConfiguration fofolaConfiguration;

    public CheckDonatorController(FofolaConfiguration fofolaConfiguration) {
        this.fofolaConfiguration = fofolaConfiguration;
    }

    @GetMapping("")
    public String getCheckDonatorPage(HttpServletRequest request, Model model) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry donator checking section.");

        RestTemplate restTemplate = new RestTemplate();
        String vcFetchUrl = fofolaConfiguration.getKrameriusHost() + "/search/api/v5.0/vc";
        List<VC> vcList = Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(vcFetchUrl, VC[].class)));

        Map<String, String> vcNameUuid = new HashMap<>();
        vcList.forEach(vc -> vcNameUuid.put(vc.descs.cs, vc.pid));
        model.addAttribute("vcList", vcNameUuid);
        return "check_donator";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<String> getAllOutputFileNames() {
        return FileUtils.getCheckDonatorOutputFileNames();
    }

    @DeleteMapping("/remove/{fileName}")
    @ResponseStatus(HttpStatus.OK)
    public void removeOutputFile(@PathVariable String fileName) {
        FileUtils.removeCheckDonatorOutputFile(fileName);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> getOutputFile(@PathVariable String fileName) throws IOException {
        File file = FileUtils.getCheckDonatorOutputFile(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
