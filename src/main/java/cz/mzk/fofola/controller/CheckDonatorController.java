package cz.mzk.fofola.controller;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.model.vc.VC;
import cz.mzk.fofola.processes.utils.FileUtils;
import cz.mzk.fofola.service.IpLogger;
import cz.mzk.fofola.service.VCUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/check-donator")
@AllArgsConstructor
@Slf4j
public class CheckDonatorController {

    private final FofolaConfiguration fofolaConfiguration;

    @GetMapping("")
    public String getCheckDonatorPage(HttpServletRequest request, Model model) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry donator checking section.");
        List<VC> vcList = VCUtils.getAllVC(fofolaConfiguration.getKrameriusHost());
        Map<String, String> vcNameUuid = VCUtils.mapAndSortVCs(vcList);
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
