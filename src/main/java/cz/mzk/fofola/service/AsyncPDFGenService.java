package cz.mzk.fofola.service;

import cz.mzk.fofola.api.KrameriusApi;
import cz.mzk.fofola.model.pdf.AsyncPDFGenLog;
import cz.mzk.fofola.model.pdf.PDFGenState;
import cz.mzk.fofola.repository.AsyncPDFGenLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncPDFGenService {

    private final AsyncPDFGenLogRepository logRepository;
    private final KrameriusApi krameriusApi;

    private final String GET_PDF_API_ENDPOINT = "/pdf/get/";

    @Async
    public void start(String uuid, String name) {
        Map<String, String> params = new HashMap<String, String>() {{
           put("pid", uuid);
           put("pageType", "TEXT");
           put("format", "A4");
        }};

        AsyncPDFGenLog genLog = new AsyncPDFGenLog();
        genLog.setUuid(uuid);
        genLog.setName(name);
        genLog.setState(PDFGenState.ACTIVE);
        logRepository.save(genLog);

        try {
            String outFilePath = FileService.getPDFOutFilePath(uuid + ".pdf");
            krameriusApi.generateAndDownloadPDF(params, outFilePath);
            genLog.setHandle(GET_PDF_API_ENDPOINT + outFilePath);
            genLog.setState(PDFGenState.FINISHED);
        } catch (IOException e) {
            genLog.setState(PDFGenState.EXCEPTION);
        }
        logRepository.save(genLog);

        log.info("Finish asynchronous PDF generating for " + uuid + ", state: " + genLog.getState().name());
    }
}
