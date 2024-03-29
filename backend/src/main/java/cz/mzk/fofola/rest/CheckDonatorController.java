package cz.mzk.fofola.rest;

import cz.mzk.fofola.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/api/check-donator")
@AllArgsConstructor
public class CheckDonatorController {

    @GetMapping("/all")
    @ResponseBody
    public List<String> getAllOutputFileNames() {
        return FileService.getCheckDonatorOutputFileNames();
    }

    @DeleteMapping("/remove/{fileName}")
    @ResponseStatus(HttpStatus.OK)
    public void removeOutputFile(@PathVariable String fileName) {
        FileService.removeCheckDonatorOutputFile(fileName);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> getOutputFile(@PathVariable String fileName) throws IOException {
        File file = FileService.getCheckDonatorOutputFile(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
