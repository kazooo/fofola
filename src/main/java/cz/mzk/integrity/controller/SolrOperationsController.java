package cz.mzk.integrity.controller;

import cz.mzk.integrity.model.CheckProcess;
import cz.mzk.integrity.model.UuidProblem;
import cz.mzk.integrity.repository.ProblemRepository;
import cz.mzk.integrity.repository.ProcessRepository;
import cz.mzk.integrity.service.AsynchronousService;
import cz.mzk.integrity.service.SolrCommunicator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class SolrOperationsController {

    private static final Logger logger = Logger.getLogger(SolrOperationsController.class.getName());

    private final SolrCommunicator solrCommunicator;
    private final AsynchronousService asynchronousService;
    private final ProcessRepository processRepository;
    private final ProblemRepository problemRepository;

    public SolrOperationsController(SolrCommunicator solrCommunicator,
                                    AsynchronousService asynchronousService,
                                    ProcessRepository processRepository,
                                    ProblemRepository problemRepository) {

        this.solrCommunicator = solrCommunicator;
        this.asynchronousService = asynchronousService;
        this.processRepository = processRepository;
        this.problemRepository = problemRepository;
    }


    @GetMapping("/check_solr_integrity")
    public String checkSolrIntegrity(Model model) {

        CheckProcess process = processRepository.findFirstByProcessType(CheckProcess.CHECK_SOLR_TYPE);
        boolean runningProcess = process != null && process.isRunning();

        List<UuidProblem> problems = problemRepository.findAll();
        boolean problemsExist = problems != null && !problems.isEmpty();

        model.addAttribute("running_process", runningProcess);
        model.addAttribute("problems_exist", problemsExist);

        if (!runningProcess && !problemsExist) {
            Map<String, Long> modelCount = solrCommunicator.facetSolrDocByModels();
            Long total = modelCount.get("total");
            modelCount.remove("total");
            model.addAttribute("total", total);
            model.addAttribute("modelCount", modelCount);
        } else {
            Map<String, String> uuidProblemDesc = new HashMap<>();
            if (runningProcess) {
                long processId = process.getId();
                problems = problemRepository.findByProcessId(processId);
            }
            for (UuidProblem problem : problems) {
                uuidProblemDesc.put(problem.getUuid(), problem.getProblemType());
            }
            model.addAttribute("done", asynchronousService.getCheckSolrStatusDone());
            model.addAttribute("total", asynchronousService.getCheckSolrStatusTotal());
            model.addAttribute("problems", uuidProblemDesc);
        }

        return "check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=run")
    public String runSolrIntegrityChecker(
            @RequestParam(name = "model", required = true) String model,
            @RequestParam(name = "docCount", required = true) long docCount) {
        logger.info("Run Solr integrity checking process.");
        CheckProcess solrProcess = new CheckProcess(CheckProcess.CHECK_SOLR_TYPE, model, docCount);
        solrProcess = processRepository.save(solrProcess);
        asynchronousService.runSolrChecking(solrProcess.getId(), model, docCount);
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=stop")
    public String stopSolrIntegrityChecker() {
        logger.info("Stop Solr integrity checking.");
        CheckProcess process = processRepository.findFirstByProcessType(CheckProcess.CHECK_SOLR_TYPE);
        process.stop();
        processRepository.saveAndFlush(process);
        asynchronousService.stopSolrChecking();
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=clear")
    public String clearProblems() {
        logger.info("Clear Solr integrity problem list.");
        CheckProcess process = processRepository.findFirstByProcessType(CheckProcess.CHECK_SOLR_TYPE);
        processRepository.delete(process);
        problemRepository.deleteAll();
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=download")
    public ResponseEntity downloadListWithProblems() throws IOException {
        logger.info("Clear Solr integrity problem list.");
        CheckProcess process = processRepository.findFirstByProcessType(CheckProcess.CHECK_SOLR_TYPE);

        List<UuidProblem> problems = problemRepository.findByProcessId(process.getId());

        List<String> notStoredUuids = new ArrayList<>();
        for (UuidProblem problem : problems) {
            if (problem.getProblemType().equals(UuidProblem.NOT_STORED)) {
                notStoredUuids.add(problem.getUuid());
            }
        }

        String notStoredFilePath = "/tmp/" + UuidProblem.NOT_STORED_FILE_NAME;
        writeUuidsIntoFile(notStoredFilePath, notStoredUuids);

        File file = new File(notStoredFilePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private void writeUuidsIntoFile(String filename, List<String> uuids) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String uuid : uuids) {
            writer.write(uuid + "\n");
        }
        writer.close();
    }
}
