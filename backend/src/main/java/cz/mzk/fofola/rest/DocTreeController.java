package cz.mzk.fofola.rest;

import com.google.gson.Gson;
import cz.mzk.fofola.constants.AccessType;
import cz.mzk.fofola.model.DocTreeNode;
import cz.mzk.fofola.model.solr.SearchDoc;
import cz.mzk.fofola.repository.SolrRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class DocTreeController {

    private final Gson gson = new Gson();
    private final SolrRepository<SearchDoc> solrRepository;

    @GetMapping("/tree/{uuid}")
    @ResponseBody
    public String generateDocTree(@PathVariable String uuid) {
        log.info("Checking: " + uuid);
        DocTreeNode root = generateNode(uuid);
        return gson.toJson(root);
    }

    private DocTreeNode generateNode(String uuid) {
        DocTreeNode node = new DocTreeNode(uuid);
        SearchDoc doc = solrRepository.getByUuid(uuid);

        if (doc == null) {
            return null;
        }

        node.setIndexed("true");
        node.setVisibilitySolr(doc.getAccessibility() == AccessType.PUBLIC);
        node.setStored("true");
        node.setImageUrl("");
        node.setName(doc.getModel() + " : " + doc.getTitle());
        node.setModel(doc.getModel());

        node.checkProblems();
        return node;
    }
}
