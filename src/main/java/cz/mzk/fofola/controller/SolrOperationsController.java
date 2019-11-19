package cz.mzk.fofola.controller;

import cz.mzk.fofola.model.FofolaProcess;
import cz.mzk.fofola.model.UuidProblem;
import cz.mzk.fofola.model.UuidProblemRecord;
import cz.mzk.fofola.repository.ProblemRepository;
import cz.mzk.fofola.service.AsynchronousFofolaProcessService;
import cz.mzk.fofola.service.FileService;
import cz.mzk.fofola.service.IpLogger;
import cz.mzk.fofola.service.SolrCommunicator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
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
    public String checkSolrIntegrity(Model model, HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry Solr index checking section.");
        Object pageOffsetAttr = request.getSession().getAttribute("page_num");
        int pageOffset = pageOffsetAttr != null ? (int) pageOffsetAttr : 0;
        int pageMax = 100;

        FofolaProcess process = asynchronousFofolaProcessService.getProcess(FofolaProcess.CHECK_SOLR_TYPE);
        boolean runningProcess = process != null && process.isRunning();

        List<UuidProblemRecord> problems = problemRepository
                .findAll(PageRequest.of(pageOffset, pageMax)).getContent();
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
                problems = problemRepository
                        .findAllByProcessId(processId, PageRequest.of(pageOffset, pageMax));
            }
            for (UuidProblemRecord problem : problems) {
                List<UuidProblem> problemTypes = problem.getProblems();
                uuidProblemDesc.put(problem, generateShortProblemDesc(problemTypes));
            }
            model.addAttribute("model", asynchronousFofolaProcessService.getCheckSolrModel());
            model.addAttribute("done", asynchronousFofolaProcessService.getCheckSolrStatusDone());
            model.addAttribute("total", asynchronousFofolaProcessService.getCheckSolrStatusTotal());
            model.addAttribute("problem_amount", uuidProblemDesc.size());
            model.addAttribute("problems", uuidProblemDesc);
        }

        return "check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=run")
    public String runSolrIntegrityChecker(
            @RequestParam(name = "model", required = true) String model,
            @RequestParam(name = "docCount", required = true) long docCount, HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Run Solr index checking.");
        asynchronousFofolaProcessService.runSolrChecking(model, docCount);
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=stop")
    public String stopSolrIntegrityChecker(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Stop Solr index checking.");
        asynchronousFofolaProcessService.stopSolrChecking();
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=clear")
    public String clearProblems(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Clear Solr index checking output.");
        asynchronousFofolaProcessService.clearSolrChecking();
        problemRepository.deleteAll();
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=download")
    public ResponseEntity downloadListWithProblems(HttpServletRequest request) throws IOException {
        IpLogger.logIp(request.getRemoteAddr(), "Download Solr index checking output.");
        FofolaProcess process = asynchronousFofolaProcessService.getProcess(FofolaProcess.CHECK_SOLR_TYPE);
        List<UuidProblemRecord> problems = problemRepository.findByProcessId(process.getId());

        Map<String, List<String>> problemTypeUuidMap = new HashMap<>();

        // sort by problem type
        for (UuidProblemRecord problem : problems) {
            List<UuidProblem> problemTypes = problem.getProblems();

            for (UuidProblem p : problemTypes) {

                String listName = UuidProblemRecord.fileNameByType.get(p.getType());
                if (!problemTypeUuidMap.containsKey(listName)) {
                    problemTypeUuidMap.put(listName, new ArrayList<>());
                }
                problemTypeUuidMap.get(listName).add(problem.getUuid());
            }
        }

        // save lists into files
        for (Map.Entry<String, List<String>> entry : problemTypeUuidMap.entrySet()) {
            String filePath = "/tmp/problems/" + entry.getKey();
            FileService.writeUuidsIntoFile(filePath, entry.getValue());
        }

        FileService.zipFolder("/tmp/problems", "/tmp/problems.zip");
        return FileService.sendFile("/tmp/" + asynchronousFofolaProcessService.getCheckSolrModel() + "_problems.zip");
    }

    @GetMapping("/generate_sitemap")
    public String generateSitemapHome(Model model, HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry sitemap generation section.");
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
    public String runSitemapGeneration(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Run sitemap generation.");
        new File(pathToSitemaps).mkdir();
        asynchronousFofolaProcessService.runSitemapGenerationProcess(pathToSitemaps);
        return "redirect:/generate_sitemap";
    }

    @PostMapping(value = "/generate_sitemap", params = "action=stop")
    public String stopSitemapGeneration(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Stop sitemap generation.");
        asynchronousFofolaProcessService.stopSitemapGeneration();
        return "redirect:/generate_sitemap";
    }

    @PostMapping(value = "/generate_sitemap", params = "action=download")
    public ResponseEntity downloadGeneratedSitemap(HttpServletRequest request) throws IOException {
        IpLogger.logIp(request.getRemoteAddr(), "Download sitemap generation output.");
        String outZipFileName = pathToSitemaps + ".zip";
        FileService.zipFolder(pathToSitemaps, outZipFileName);
        return FileService.sendFile(outZipFileName);
    }

    @PostMapping(value = "/generate_sitemap", params = "action=clear")
    public String clearGeneratedSitemaps(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Clear sitemap generation output.");
        FileService.deleteDir(pathToSitemaps);
        asynchronousFofolaProcessService.clearSitemapGen();
        return "redirect:/generate_sitemap";
    }

    private String generateShortProblemDesc(List<UuidProblem> problems) {
        return problems.stream().map(UuidProblem::getShortDesc)
                .collect(Collectors.joining(", "));
    }
}
