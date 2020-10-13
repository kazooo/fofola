package cz.mzk.fofola.controller;

import com.google.gson.Gson;
import cz.mzk.fofola.model.DocTreeNode;
import cz.mzk.fofola.model.FedoraDocument;
import cz.mzk.fofola.model.SolrDocument;
import cz.mzk.fofola.repository.FedoraDocumentRepository;
import cz.mzk.fofola.repository.SolrDocumentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
@Slf4j
public class DocTreeController {

    private final Gson gson = new Gson();
    private final SolrDocumentRepository solrRepository;
    private final FedoraDocumentRepository fedoraRepository;
    private final Map<String, List<String>> solrChildrenBuffer = new LinkedHashMap<String, List<String>>() {
        @Override protected boolean removeEldestEntry(Map.Entry<String, List<String>> entry) {
            return size() > 10;
        }
    };

    @GetMapping("/tree/{uuid}")
    @ResponseBody
    public String generateDocTree(@PathVariable String uuid) {
        // uuid must not be root
        SolrDocument solrRootDoc = solrRepository.getByUuid(uuid);
        String rootUuid = solrRootDoc.getUuid();

        log.info("Checking: " + rootUuid);
        DocTreeNode root = generateNode(rootUuid);
        log.info("Finish checking: " + rootUuid);

        return gson.toJson(root);
    }

    private DocTreeNode generateNode(String uuid) {
        DocTreeNode node = new DocTreeNode(uuid);
        SolrDocument solrDoc = solrRepository.getByUuid(uuid);
        FedoraDocument fedoraDoc = fedoraRepository.getFedoraDocByUuid(uuid);

        fillFromSolrDoc(node, solrDoc);
        fillFromFedoraDoc(node, fedoraDoc);
        fillFromOptional(node, fedoraDoc, solrDoc);
        processChildren(node, fedoraDoc);

        node.checkProblems();
        return node;
    }

    private void fillFromFedoraDoc(DocTreeNode node, FedoraDocument fedoraDoc) {
        if (fedoraDoc == null) {
            node.setStored("false");
            node.setVisibilityFedora("unknown");
            node.setImageUrl("no_image");
        } else {
            node.setStored("true");
            node.setVisibilityFedora(fedoraDoc.getAccesibility());
            node.setImageUrl(fedoraDoc.getImageUrl());
        }
    }

    private void fillFromSolrDoc(DocTreeNode node, SolrDocument solrDoc) {
        if (solrDoc == null) {
            node.setIndexed("false");
            node.setVisibilitySolr("unknown");
        } else {
            node.setIndexed("true");
            node.setVisibilitySolr(solrDoc.getAccessibility());
        }
    }

    private void fillFromOptional(DocTreeNode node, FedoraDocument fedoraDoc, SolrDocument solrDoc) {
        if (solrDoc != null) {
            node.setName(solrDoc.getModel() + " : " + solrDoc.getDcTitle());
            node.setModel(solrDoc.getModel());
        } else if (fedoraDoc != null) {
            node.setName(fedoraDoc.getModel() + " : " + fedoraDoc.getTitle());
            node.setModel(fedoraDoc.getModel());
        }
    }

    private void processChildren(DocTreeNode node, FedoraDocument fedoraDoc) {
        List<String> fedoraChildUuids = null;
        if (fedoraDoc != null) {
            fedoraChildUuids = fedoraDoc.getChildUuids();
            fedoraChildUuids.forEach(childUuid -> {
                DocTreeNode childNode = generateNode(childUuid);
                childNode.setLinkInRelsExt("true");
                node.addChild(childNode);
            });
        }

        List<String> solrChildUuids;
        if (solrChildrenBuffer.containsKey(node.uuid)) {
            solrChildUuids = solrChildrenBuffer.get(node.uuid);
        } else {
            solrChildUuids = solrRepository
                    .getChildByParentUuid(node.uuid).stream()
                    .map(SolrDocument::getUuid)
                    .collect(Collectors.toList());
            if (fedoraChildUuids != null) {
                solrChildUuids.removeAll(fedoraChildUuids);
            }
            solrChildrenBuffer.put(node.uuid, solrChildUuids);
        }

        solrChildUuids.forEach(childUuid -> {
            DocTreeNode childNode = generateNode(childUuid);
            childNode.setLinkInRelsExt("false");
            node.addChild(childNode);
        });
    }
}
