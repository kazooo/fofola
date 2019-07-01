package cz.mzk.integrity.controller;

import cz.mzk.integrity.service.AsynchronousService;
import cz.mzk.integrity.service.SolrCommunicator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.logging.Logger;

@Controller
public class SolrController {

    private static final Logger logger = Logger.getLogger(SolrController.class.getName());

    private final SolrCommunicator solrCommunicator;
    private final AsynchronousService asynchronousService;

    public SolrController(SolrCommunicator solrCommunicator,
                          AsynchronousService asynchronousService) {
        this.solrCommunicator = solrCommunicator;
        this.asynchronousService = asynchronousService;
    }


    @GetMapping("/check_solr_integrity")
    public String checkSolrIntegrity(Model model) {

        Map<String, Long> modelCount = solrCommunicator.facetSolrDocByModels();
        model.addAttribute("modelCount", modelCount);
        return "check_solr_integrity";
    }

    @PostMapping("/check_solr_integrity")
    public String runSolrIntegrityChecker(
            @RequestParam(name = "model", required = true) String model,
            @RequestParam(name = "docCount", required = true) long docCount) {
        asynchronousService.runSolrChecking(model, docCount);
        return "redirect:/check_solr_integrity";
    }
}
