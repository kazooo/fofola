package cz.mzk.integrity.controller;

import com.google.gson.Gson;
import cz.mzk.integrity.model.DocTreeModel;
import cz.mzk.integrity.model.FedoraDocument;
import cz.mzk.integrity.model.SolrDocument;
import cz.mzk.integrity.service.FedoraCommunicator;
import cz.mzk.integrity.service.SolrCommunicator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Controller
public class DocTreeController {

    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(SolrOperationsController.class.getName());

    private final SolrCommunicator solrCommunicator;
    private final FedoraCommunicator fedoraCommunicator;

    public DocTreeController(SolrCommunicator solrCommunicator, FedoraCommunicator fedoraCommunicator) {
        this.solrCommunicator = solrCommunicator;
        this.fedoraCommunicator = fedoraCommunicator;
    }

    @GetMapping("/tree")
    public String home() {
        return "tree_page";
    }


    @PostMapping("/tree")
    public String getTree(Model model, @RequestParam(name = "uuid", required = true) String uuid) {

        List<SolrDocument> docs = solrCommunicator.getSolrDocsByRootPid(uuid);
        DocTreeModel tree = generateTree(docs, uuid);
        String json = gson.toJson(tree);
        model.addAttribute("tree_data", json);
        return "tree_page";
    }

    private DocTreeModel generateTree(List<SolrDocument> docs, String parentUuid) {
        SolrDocument parentDoc = docs.stream().filter(d -> d.getUuid().equals(parentUuid)).findFirst().orElse(null);
        FedoraDocument fedoraDoc = fedoraCommunicator.getFedoraDocByUuid(parentUuid);
        assert parentDoc != null;

        String nodeName = parentDoc.getModel() + " : " + parentDoc.getDcTitle();
        DocTreeModel parentNode = new DocTreeModel(nodeName);
        parentNode.setUuid(parentUuid);
        parentNode.setVisibilitySolr(parentDoc.getAccessibility());
        parentNode.setModel(parentDoc.getModel());

        if (fedoraDoc != null) {
            parentNode.setStored("true");
            parentNode.setVisibilityFedora(fedoraDoc.getAccesibility());
            parentNode.setImageUrl(fedoraDoc.getImageUrl());
        }

        // filter children for given parent
        Stream<SolrDocument> childs = docs.stream()
                .filter(d -> d.getParentPids().contains(parentUuid))
                .sorted(Comparator.comparing(o -> o.getRelsExtIndexForParent(parentUuid)));
        // recursively get children of children and add it to parent generating tree
        childs.forEach(c -> {
            String uuid = c.getUuid();
            if (!uuid.equals(parentUuid)) {
                parentNode.addChild(generateTree(docs, uuid));
            }
        });
        return parentNode;
    }
}
