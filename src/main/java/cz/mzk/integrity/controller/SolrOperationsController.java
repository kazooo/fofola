package cz.mzk.integrity.controller;

import cz.mzk.integrity.model.FofolaProcess;
import cz.mzk.integrity.model.UuidProblem;
import cz.mzk.integrity.model.UuidProblemRecord;
import cz.mzk.integrity.repository.ProblemRepository;
import cz.mzk.integrity.service.AsynchronousFofolaProcessService;
import cz.mzk.integrity.service.FileService;
import cz.mzk.integrity.service.SolrCommunicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class SolrOperationsController {

    private static final Logger logger = Logger.getLogger(SolrOperationsController.class.getName());

    private final SolrCommunicator solrCommunicator;
    private final AsynchronousFofolaProcessService asynchronousFofolaProcessService;
    private final ProblemRepository problemRepository;

    private static final String sitemapDirName = "sitemaps";
    private static final String pathToSitemaps = "/tmp/" + sitemapDirName;

    public SolrOperationsController(SolrCommunicator solrCommunicator,
                                    AsynchronousFofolaProcessService asynchronousFofolaProcessService,
                                    ProblemRepository problemRepository) {

        this.solrCommunicator = solrCommunicator;
        this.asynchronousFofolaProcessService = asynchronousFofolaProcessService;
        this.problemRepository = problemRepository;
    }


    @GetMapping("/check_solr_integrity")
    public String checkSolrIntegrity(Model model) {

        FofolaProcess process = asynchronousFofolaProcessService.getProcess(FofolaProcess.CHECK_SOLR_TYPE);
        boolean runningProcess = process != null && process.isRunning();

        List<UuidProblemRecord> problems = problemRepository.findAll();
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
            Map<UuidProblemRecord, String> uuidProblemDesc = new HashMap<>();
            if (runningProcess) {
                long processId = process.getId();
                problems = problemRepository.findByProcessId(processId);
            }
            for (UuidProblemRecord problem : problems) {
                List<UuidProblem> problemTypes = problem.getProblems();
                uuidProblemDesc.put(problem, generateShortProblemDesc(problemTypes));
            }
            model.addAttribute("done", asynchronousFofolaProcessService.getCheckSolrStatusDone());
            model.addAttribute("total", asynchronousFofolaProcessService.getCheckSolrStatusTotal());
            model.addAttribute("problems", uuidProblemDesc);
        }

        return "check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=run")
    public String runSolrIntegrityChecker(
            @RequestParam(name = "model", required = true) String model,
            @RequestParam(name = "docCount", required = true) long docCount) {
        asynchronousFofolaProcessService.runSolrChecking(model, docCount);
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=stop")
    public String stopSolrIntegrityChecker() {
        asynchronousFofolaProcessService.stopSolrChecking();
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=clear")
    public String clearProblems() {
        asynchronousFofolaProcessService.clearSolrChecking();
        problemRepository.deleteAll();
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=download")
    public ResponseEntity downloadListWithProblems() throws IOException {
        logger.info("Download Solr integrity problem list.");
        FofolaProcess process = asynchronousFofolaProcessService.getProcess(FofolaProcess.CHECK_SOLR_TYPE);

        List<UuidProblemRecord> problems = problemRepository.findByProcessId(process.getId());

        List<String> notStoredUuids = new ArrayList<>();
        for (UuidProblemRecord problem : problems) {
            List<UuidProblem> problemTypes = problem.getProblems();
            for (UuidProblem p : problemTypes) {
                if (p.getType().equals(UuidProblem.NOT_STORED)) {
                    notStoredUuids.add(problem.getUuid());
                }
            }
        }

        String notStoredFilePath = "/tmp/" + UuidProblemRecord.NOT_STORED_FILE_NAME;
        FileService.writeUuidsIntoFile(notStoredFilePath, notStoredUuids);
        return FileService.sendFile(notStoredFilePath);
    }

    @GetMapping("/generate_sitemap")
    public String generateSitemapHome(Model model) {
        FofolaProcess process = asynchronousFofolaProcessService.getProcess(FofolaProcess.GENERATE_SITEMAP_TYPE);
        boolean runningGeneration = process != null && process.isRunning();
        model.addAttribute("running_generation", runningGeneration);

        boolean hasGenerated = new File(pathToSitemaps).exists() && !runningGeneration;
        model.addAttribute("has_generated", hasGenerated);

        if (runningGeneration) {
            model.addAttribute("done", asynchronousFofolaProcessService.getSitemapGenStatusDone());
            model.addAttribute("total", asynchronousFofolaProcessService.getSitemapGenStatusTotal());
        }
        return "generate_sitemap";
    }

    @PostMapping(value = "/generate_sitemap", params = "action=run")
    public String runSitemapGeneration() throws IOException {
        Files.createTempDirectory(sitemapDirName);
        asynchronousFofolaProcessService.runSitemapGenerationProcess(pathToSitemaps);
        return "redirect:/generate_sitemap";
    }

    @PostMapping(value = "/generate_sitemap", params = "action=stop")
    public String stopSitemapGeneration() {
        asynchronousFofolaProcessService.stopSitemapGeneration();
        return "redirect:/generate_sitemap";
    }

    @PostMapping(value = "/generate_sitemap", params = "action=download")
    public ResponseEntity downloadGeneratedSitemap() throws IOException {
        String outZipFileName = pathToSitemaps + ".zip";
        FileService.zipFolder(pathToSitemaps, outZipFileName);
        return FileService.sendFile(outZipFileName);
    }

    @PostMapping(value = "/generate_sitemap", params = "action=clear")
    public String clearGeneratedSitemaps() {
        FileService.deleteDir(pathToSitemaps);
        asynchronousFofolaProcessService.clearSitemapGen();
        return "redirect:/generate_sitemap";
    }

    private String generateShortProblemDesc(List<UuidProblem> problems) {
        return problems.stream().map(UuidProblem::getShortDesc)
                .collect(Collectors.joining(", "));
    }
}
