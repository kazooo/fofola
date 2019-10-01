package cz.mzk.integrity.controller;

import com.google.gson.Gson;
import cz.mzk.integrity.model.DocTreeModel;
import cz.mzk.integrity.model.FedoraDocument;
import cz.mzk.integrity.model.SolrDocument;
import cz.mzk.integrity.model.UuidProblem;
import cz.mzk.integrity.service.FedoraCommunicator;
import cz.mzk.integrity.service.IpLogger;
import cz.mzk.integrity.service.SolrCommunicator;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    public String home(HttpServletRequest request) {
        IpLogger.logIp(request.getRemoteAddr(), "Entry document tree section.");
        return "tree_page";
    }

    @MessageMapping("/tree-websocket")
    @SendTo("/tree/data")
    public String getTreeDataWebSocket(String uuid, SimpMessageHeaderAccessor ha) {
        // uuid must not be root
        String rootUuid = getRoot(uuid);
        IpLogger.logIp((String) ha.getSessionAttributes().get("IP"), "Checking: " + rootUuid);
        List<SolrDocument> docs = new ArrayList<>();
        docs.addAll(solrCommunicator.getSolrDocsByRootPid(rootUuid));
        DocTreeModel tree = generateTree(docs, rootUuid);
        IpLogger.logIp((String) ha.getSessionAttributes().get("IP"), "Finish checking: " + rootUuid);
        return gson.toJson(tree);
    }

    private DocTreeModel generateTree(List<SolrDocument> docs, String parentUuid) {
        SolrDocument parentDoc = docs.stream().filter(d -> d.getUuid().equals(parentUuid)).findFirst().orElse(null);
        assert parentDoc != null;
        docs.remove(parentDoc);
        FedoraDocument fedoraDoc = fedoraCommunicator.getFedoraDocByUuid(parentUuid);

        String nodeName = parentDoc.getModel() + " : " + parentDoc.getDcTitle();
        DocTreeModel parentNode = new DocTreeModel(nodeName);
        parentNode.setUuid(parentUuid);
        parentNode.setVisibilitySolr(parentDoc.getAccessibility());
        parentNode.setModel(parentDoc.getModel());

        // filter children for given parent
        Supplier<Stream<SolrDocument>> childs = () -> docs.stream()
                .filter(d -> d.getParentPids().contains(parentUuid))
                .sorted(Comparator.comparing(o -> o.getRelsExtIndexForParent(parentUuid)));

        if (fedoraDoc != null) {
            parentNode.setStored("true");
            parentNode.setVisibilityFedora(fedoraDoc.getAccesibility());
            parentNode.setImageUrl(fedoraDoc.getImageUrl());
            checkFedoraChilds(parentNode, fedoraDoc, childs);
            fedoraDoc = null;
        } else {
            parentNode.setStored("false");
            parentNode.setVisibilityFedora("unknown");
            parentNode.setImageUrl(UuidProblem.NO_IMAGE);
            parentNode.hasProblem = true;
        }

        docCounter++;
        if (docCounter >= docsToCallGC) {
            // requesting JVM for running Garbage Collector
            logger.info("Run system garbage collector...");
            System.gc();
            docCounter = 0;
        }

        // recursively get children of children and add it to parent generating tree
        childs.get().forEach(c -> {
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

    private void checkFedoraChilds(DocTreeModel parentNode, FedoraDocument fedoraDoc, Supplier<Stream<SolrDocument>> childs) {
        List<String> fedoraChilds = fedoraDoc.getChilds();
        List<String> missInSolr = childs.get()
                .filter(d -> !fedoraChilds.contains(d.getUuid()))
                .map(SolrDocument::getUuid)
                .collect(Collectors.toList());

        if (!missInSolr.isEmpty()) {
            parentNode.hasProblematicChild = true;
        }

        for (String childUuid : missInSolr) {
            FedoraDocument childFedoraDoc = fedoraCommunicator.getFedoraDocByUuid(childUuid);

            DocTreeModel childNode = new DocTreeModel(childUuid);
            childNode.setUuid(childUuid);
            childNode.setIndexed("false");
            childNode.setVisibilitySolr("unknown");
            childNode.hasProblem = true;

            if (childFedoraDoc != null) {
                childNode.setModel(childFedoraDoc.getModel());
                childNode.setStored("true");
                childNode.setVisibilityFedora(childFedoraDoc.getAccesibility());
                childNode.setImageUrl(childFedoraDoc.getImageUrl());
                childFedoraDoc = null;
            } else {
                childNode.setModel("unknown");
                childNode.setStored("false");
                childNode.setVisibilityFedora("unknown");
                childNode.setImageUrl(UuidProblem.NO_IMAGE);
            }

            parentNode.addChild(childNode);
            docCounter++;
        }
    }
}
