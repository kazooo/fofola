package cz.mzk.fofola.controller;

import com.google.gson.Gson;
import cz.mzk.fofola.model.DocTreeModel;
import cz.mzk.fofola.model.FedoraDocument;
import cz.mzk.fofola.model.SolrDocument;
import cz.mzk.fofola.repository.FedoraDocumentRepository;
import cz.mzk.fofola.service.IpLogger;
import cz.mzk.fofola.repository.SolrDocumentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@AllArgsConstructor
@Slf4j
public class DocTreeController {

    private static int docCounter = 0;
    private static int docsToCallGC = 3000;
    private static final Gson gson = new Gson();

    private final SolrDocumentRepository solrDocumentRepository;
    private final FedoraDocumentRepository fedoraDocumentRepository;

    @MessageMapping("/tree-websocket")
    @SendTo("/tree/data")
    public String getTreeDataWebSocket(String uuid, SimpMessageHeaderAccessor ha) {
        // uuid must not be root
        String rootUuid = getRoot(uuid);
        Object ipAdress = ha.getSessionAttributes().get("IP");
        IpLogger.logIp(ipAdress.toString(), "Checking: " + rootUuid);
        List<SolrDocument> docs = new ArrayList<>(solrDocumentRepository.getByRootPid(rootUuid));
        DocTreeModel tree = generateTree(docs, rootUuid);
        IpLogger.logIp(ipAdress.toString(), "Finish checking: " + rootUuid);
        return gson.toJson(tree);
    }

    private DocTreeModel generateTree(List<SolrDocument> docs, String parentUuid) {
        SolrDocument parentDoc = docs.stream().filter(d -> d.getUuid().equals(parentUuid)).findFirst().orElse(null);
        assert parentDoc != null;
        docs.remove(parentDoc);
        FedoraDocument fedoraDoc = fedoraDocumentRepository.getFedoraDocByUuid(parentUuid);

        String nodeName = parentDoc.getModel() + " : " + parentDoc.getDcTitle();
        DocTreeModel parentNode = new DocTreeModel(nodeName);
        parentNode.setUuid(parentUuid);
        parentNode.setVisibilitySolr(parentDoc.getAccessibility());
        parentNode.setModel(parentDoc.getModel());

        // filter children for given parent
        Supplier<Stream<SolrDocument>> childs = () -> docs.stream()
                .filter(d -> d.getParentPids().contains(parentUuid))
                .sorted(Comparator.comparing(o -> o.getRelsExtIndexForParent(parentUuid)));

        List<String> notInRelsExt = null;
        if (fedoraDoc != null) {
            parentNode.setStored("true");
            parentNode.setVisibilityFedora(fedoraDoc.getAccesibility());
            parentNode.setImageUrl(fedoraDoc.getImageUrl());
            notInRelsExt = checkFedoraChilds(parentNode, fedoraDoc, childs);
            fedoraDoc = null;
        } else {
            parentNode.setStored("false");
            parentNode.setVisibilityFedora("unknown");
            parentNode.setImageUrl("no_image");
            parentNode.hasProblem = true;
        }

        docCounter++;
        if (docCounter >= docsToCallGC) {
            // requesting JVM for running Garbage Collector
            log.info("Run system garbage collector...");
            System.gc();
            docCounter = 0;
        }

        // recursively get children of children and add it to parent generating tree
        List<String> finalNotInRelsExt = notInRelsExt;
        childs.get().forEach(c -> {
            String uuid = c.getUuid();
            if (!uuid.equals(parentUuid)) {
                DocTreeModel childNode = generateTree(docs, uuid);
                if (finalNotInRelsExt != null && finalNotInRelsExt.contains(uuid)) {
                    childNode.setLinkInRelsExt("false");
                }
                parentNode.addChild(childNode);
            }
        });

        parentNode.checkProblems();
        return parentNode;
    }

    private String getRoot(String uuid) {
        SolrDocument doc = solrDocumentRepository.getByUuid(uuid);
        return doc.getRootPid();
    }

    private List<String> checkFedoraChilds(DocTreeModel parentNode, FedoraDocument fedoraDoc,
                                   Supplier<Stream<SolrDocument>> childs) {
        List<String> fedoraChilds = fedoraDoc.getChilds();

        List<String> notInRelsExt = childs.get()
                .filter(d -> !fedoraChilds.contains(d.getUuid()))
                .map(SolrDocument::getUuid)
                .collect(Collectors.toList());

        List<String> missInSolr = fedoraChilds.stream()
                .filter(f -> childs.get().noneMatch(s -> s.getUuid().equals(f)))
                .collect(Collectors.toList());

        if (!missInSolr.isEmpty()) {
            parentNode.hasProblematicChild = true;
        }

        for (String childUuid : missInSolr) {
            FedoraDocument childFedoraDoc = fedoraDocumentRepository.getFedoraDocByUuid(childUuid);

            DocTreeModel childNode = new DocTreeModel(childUuid);
            childNode.setUuid(childUuid);
            childNode.setIndexed("false");
            childNode.setVisibilitySolr("unknown");
            childNode.hasProblem = true;
            childNode.setLinkInRelsExt("false");

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
                childNode.setImageUrl("no_image");
            }

            parentNode.addChild(childNode);
            docCounter++;
        }

        return notInRelsExt;
    }
}
