package cz.mzk.integrity.controller;

import com.google.gson.Gson;
import cz.mzk.integrity.model.DocTreeModel;
import cz.mzk.integrity.model.FedoraDocument;
import cz.mzk.integrity.model.SolrDocument;
import cz.mzk.integrity.model.UuidProblem;
import cz.mzk.integrity.service.FedoraCommunicator;
import cz.mzk.integrity.service.SolrCommunicator;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Controller
public class DocTreeController {

    private static int docCounter = 0;
    private static int docsToCallGC = 1000;

    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(DocTreeController.class.getName());

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

    @MessageMapping("/tree-websocket")
    @SendTo("/tree/data")
    public String getTreeDataWebSocket(String uuid) {
        // uuid must not be root
        String rootUuid = getRoot(uuid);
        List<SolrDocument> docs = solrCommunicator.getSolrDocsByRootPid(rootUuid);
        DocTreeModel tree = generateTree(docs, rootUuid);
        return gson.toJson(tree);
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
            fedoraDoc = null;
        } else {
            parentNode.setStored("false");
            parentNode.setVisibilityFedora("unknown");
            parentNode.setImageUrl(UuidProblem.NO_IMAGE);
        }

        docCounter++;
        if (docCounter >= docsToCallGC) {
            // requesting JVM for running Garbage Collector
            logger.info("Run system garbage collector...");
            System.gc();
            docCounter = 0;
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

        parentNode.checkProblems();
        return parentNode;
    }

    private String getRoot(String uuid) {
        SolrDocument doc = solrCommunicator.getSolrDocByUuid(uuid);
        return doc.getRootPid();
    }
}
