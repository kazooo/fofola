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
import java.util.List;

@Controller
@RequestMapping("/api/solr-response")
@AllArgsConstructor
public class SolrResponseWritingController {

    @GetMapping("/all")
    @ResponseBody
    public List<String> getAllOutputFileNames() {
        return FileService.getSolrRespOutputFileNames();
    }

    @DeleteMapping("/remove/{fileName}")
    @ResponseStatus(HttpStatus.OK)
    public void removeOutputFile(@PathVariable String fileName) {
        FileService.removeSolrRespOutputFile(fileName);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> getOutputFile(@PathVariable String fileName) throws IOException {
        File file = FileService.getSolrRespOutputFile(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
