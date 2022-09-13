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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncPDFGenService {

    private final AsyncPDFGenLogRepository logRepository;
    private final KrameriusApi krameriusApi;

    private final String GET_PDF_API_ENDPOINT = "/pdf/get/";

    @Async("pdfServiceThreadExecutor")
    public void start(String uuid, String name, AsyncPDFGenLog genLog) {
        Map<String, String> params = prepareParams(uuid);
        String outFilePath = FileService.getPDFOutFilePath(uuid + ".pdf");
        saveActiveLog(genLog);

        boolean success = generateAndDownloadPdf(params, outFilePath);
        if (success) {
            saveFinishedLog(genLog, outFilePath);
        } else {
            saveExceptionLog(genLog);
        }
        log.info("Finish asynchronous PDF generating for " + uuid + ", state: " + genLog.getState().name());
    }

    public AsyncPDFGenLog prepare(String uuid){
        return saveWaitingLog(uuid, null);
    }

    private Map<String, String> prepareParams(String uuid) {
        return new HashMap<>() {{
            put("pid", uuid);
            put("pageType", "TEXT");
            put("format", "A4");
        }};
    }

    private boolean generateAndDownloadPdf(Map<String, String> params, String outFilePath) {
        try {
            krameriusApi.generateAndDownloadPDF(params, outFilePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private AsyncPDFGenLog saveWaitingLog(String uuid, String name) {
        AsyncPDFGenLog genLog = new AsyncPDFGenLog();
        genLog.setState(PDFGenState.WAITING);
        genLog.setUuid(uuid);
        genLog.setName(uuid);
        genLog.setDate(new Date());
        logRepository.save(genLog);
        return genLog;
    }

    private void saveActiveLog(AsyncPDFGenLog genLog) {
        genLog.setState(PDFGenState.ACTIVE);
        logRepository.save(genLog);
    }

    private void saveFinishedLog(AsyncPDFGenLog genLog, String outFilePath) {
        genLog.setHandle(GET_PDF_API_ENDPOINT + outFilePath);
        genLog.setState(PDFGenState.FINISHED);
        logRepository.save(genLog);
    }

    private void saveExceptionLog(AsyncPDFGenLog genLog) {
        genLog.setState(PDFGenState.EXCEPTION);
        logRepository.save(genLog);
    }

    public List<AsyncPDFGenLog> getAllLogs() {
        return logRepository.findAll();
    }

    public void removeLogAndFile(Long id) {
        AsyncPDFGenLog genLog = logRepository.getById(id);
        String fileName = genLog.getUuid() + ".pdf";
        try {
            FileService.removePDFOutputFile(fileName);
        } catch (Exception ignored) {}
        finally {
            logRepository.deleteById(id);
        }
    }
}
