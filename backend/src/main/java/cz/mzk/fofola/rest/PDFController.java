package cz.mzk.fofola.rest;

import cz.mzk.fofola.model.pdf.AsyncPDFGenLog;
import cz.mzk.fofola.service.AsyncPDFGenService;
import cz.mzk.fofola.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/api/pdf")
@Slf4j
@AllArgsConstructor
public class PDFController {

    private final AsyncPDFGenService pdfGenService;

    @GetMapping("/get")
    @ResponseBody
    public List<AsyncPDFGenLog> getAllPDFGenLogs() {
        return pdfGenService.getAllLogs();
    }

    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void removePDFFile(@PathVariable Long id) {
        log.info("Remove PDF file with id: " + id);
        pdfGenService.removeLogAndFile(id);
    }

    @GetMapping("/get/{uuid}")
    public ResponseEntity<Resource> getOutputPDFFile(@PathVariable String uuid) throws IOException {
        File file = FileService.getPDFOutputFile(uuid + ".pdf");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping("/generate/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public void startPDFGenerating(@PathVariable String uuid) {
        log.info("Start asynchronous PDF generating for " + uuid);
        pdfGenService.start(uuid, null);
    }
}
