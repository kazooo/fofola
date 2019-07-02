package cz.mzk.integrity.controller;

import cz.mzk.integrity.model.Process;
import cz.mzk.integrity.model.UuidProblem;
import cz.mzk.integrity.repository.ProblemRepository;
import cz.mzk.integrity.repository.ProcessRepository;
import cz.mzk.integrity.service.AsynchronousService;
import cz.mzk.integrity.service.SolrCommunicator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class SolrController {

    private static final Logger logger = Logger.getLogger(SolrController.class.getName());

    private final SolrCommunicator solrCommunicator;
    private final AsynchronousService asynchronousService;
    private final ProcessRepository processRepository;
    private final ProblemRepository problemRepository;

    public SolrController(SolrCommunicator solrCommunicator,
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

        List<Process> processes = processRepository.findByProcessType(Process.CHECK_SOLR_TYPE);
        boolean runningProcess = processes != null && !processes.isEmpty();
        model.addAttribute("running_process", runningProcess);

        if (!runningProcess) {
            Map<String, Long> modelCount = solrCommunicator.facetSolrDocByModels();
            model.addAttribute("modelCount", modelCount);
        } else {
            Map<String, String> uuidProblemDesc = new HashMap<>();
            long processId = processes.get(0).getId();
            List<UuidProblem> problems = problemRepository.findByProcessId(processId);
            for (UuidProblem problem : problems) {
                uuidProblemDesc.put(problem.getUuid(), problem.getProblemType());
            }

            model.addAttribute("problems", uuidProblemDesc);
        }

        return "check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=run")
    public String runSolrIntegrityChecker(
            @RequestParam(name = "model", required = true) String model,
            @RequestParam(name = "docCount", required = true) long docCount) {
        Process solrProcess = new Process(Process.CHECK_SOLR_TYPE, model, docCount);
        solrProcess = processRepository.save(solrProcess);
        asynchronousService.runSolrChecking(solrProcess.getId(), model, docCount);
        return "redirect:/check_solr_integrity";
    }

    @PostMapping(value = "/check_solr_integrity", params = "action=stop")
    public String stopSolrIntegrityChecker() {
        asynchronousService.stopSolrChecking();
        List<Process> processes = processRepository.findByProcessType(Process.CHECK_SOLR_TYPE);
        processRepository.delete(processes.get(0));
        return "redirect:/check_solr_integrity";
    }
}
