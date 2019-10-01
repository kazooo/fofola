package cz.mzk.integrity.threads;

import cz.mzk.integrity.model.*;
import cz.mzk.integrity.repository.ProblemRepository;
import cz.mzk.integrity.service.FedoraCommunicator;
import cz.mzk.integrity.service.SolrCommunicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SolrIntegrityCheckerThread extends FofolaThread {

    private String model;
    private long docCount;
    private int docsPerQuery;
    private String collectionName;
    private long processId;
    private long done;

    private final SolrCommunicator solrCommunicator;
    private final FedoraCommunicator fedoraCommunicator;
    private final ProblemRepository problemRepository;

    private final Logger logger = LoggerFactory.getLogger(SolrIntegrityCheckerThread.class);

    private final List<String> mustHaveParent = Arrays.asList(
            "page", "periodicalitem",
            "periodicalvolume", "article",
            "supplement", "monographunit",
            "track", "soundunit",
            "internalpart", "picture"
    );

    private final List<String> mustHaveChild = Arrays.asList(
            "monograph", "periodical",
            "soundrecording", "archive",
            "sheetmusic", "manuscript",
            "map", "graphic",

            "periodicalitem", "periodicalvolume",
            "article", "supplement",
            "monographunit", "internalpart"
    );

    public SolrIntegrityCheckerThread(SolrCommunicator solrCommunicator,
                                      FedoraCommunicator fedoraCommunicator,
                                      ProblemRepository problemRepository,
                                      FofolaThreadEventPublisher eventPublisher) {
        super(eventPublisher);
        super.setType(FofolaProcess.CHECK_SOLR_TYPE);
        this.solrCommunicator = solrCommunicator;
        this.fedoraCommunicator = fedoraCommunicator;
        this.problemRepository = problemRepository;
    }

    public void setProcessId(long processId) {
        this.processId = processId;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setDocCount(long docCount) {
        this.docCount = docCount;
    }

    public void setDocsPerQuery(int docsPerQuery) {
        this.docsPerQuery = docsPerQuery;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public long getDone() { return done; }

    public long getDocCount() {
        return docCount;
    }

    public String getModel() { return model; }

    @Override
    protected void process() {
        SimpleQuery query = new SimpleQuery(SolrDocument.MODEL + ":" + model)
                .addSort(Sort.by(SolrDocument.ID))
                .setRows(docsPerQuery);

        done = 0;
        Set<UuidProblem> problems = new HashSet<>();
        Cursor<SolrDocument> solrDocs = solrCommunicator.getDocCursor(collectionName, query);

        while (done < docCount && super.keepProcess && solrDocs.hasNext()) {
            SolrDocument solrDoc = solrDocs.next();
            String uuid = solrDoc.getUuid();

            FedoraDocument fedoraDoc = fedoraCommunicator.getFedoraDocByUuid(uuid);

            // check if stored in Fedora
            if (fedoraDoc == null) {
                problems.add(new UuidProblem(UuidProblem.NOT_STORED));
            }

            // check model
            if (fedoraDoc != null && fedoraDoc.getModel().equals(UuidProblem.NO_MODEL)) {
                problems.add(new UuidProblem(UuidProblem.NO_MODEL));
            }

            // check if doc in Fedora has the same visibility as doc in Solr
            if (fedoraDoc != null && !fedoraDoc.getAccesibility().equals(solrDoc.getAccessibility())) {
                problems.add(new UuidProblem(UuidProblem.DIFF_VISIBILITY));
            }

            // check doc root
            checkUuidExistence(solrDoc.getRootPid(), problems,
                    UuidProblem.NO_ROOT,
                    UuidProblem.ROOT_NOT_INDEXED,
                    UuidProblem.ROOT_NOT_STORED);

            // check doc parent
            if (mustHaveParent.contains(model)) {
                checkUuidExistence(solrDoc.getParentPids().get(0), problems,
                        UuidProblem.NO_PARENT,
                        UuidProblem.PARENT_NOT_INDEXED,
                        UuidProblem.PARENT_NOT_STORED);
            }

            // check child
            if (mustHaveChild.contains(model) && fedoraDoc != null) {
                List<String> child = fedoraCommunicator.getChildrenUuids(uuid);
                for (String childUuid : child) {
                    boolean problemFound = checkUuidExistence(childUuid, problems,
                            UuidProblem.NO_CHILD,
                            UuidProblem.CHILD_NOT_INDEXED,
                            UuidProblem.CHILD_NOT_STORED);

                    if (problemFound) {
                        break;  // otherwise can be duplicities, one problem is enough to check child later...
                    }
                }
            }

            if (!problems.isEmpty()) {
                UuidProblemRecord p = new UuidProblemRecord(
                        processId, uuid, solrDoc.getRootTitle(), model,
                        new ArrayList<>(problems));
                problemRepository.save(p);
                problems.clear();
            }
            done++;
        }
    }

    private boolean checkUuidExistence(String uuid, Set<UuidProblem> problems,
                                    String notExistProblemType,
                                    String notStoredProblemType,
                                    String notIndexedProblemType) {

        // check if uuid exists
        if (uuid == null || uuid.isEmpty()) {
            problems.add(new UuidProblem(notExistProblemType));
            return true;  // problem found
        }

        boolean problemFound = false;

        // check if uuid indexed
        SolrDocument uuidSolrDoc = solrCommunicator.getSolrDocByUuid(uuid);
        if (uuidSolrDoc == null) {
            problemFound = true;
            problems.add(new UuidProblem(notIndexedProblemType));
        }

        // check if uuid stored
        FedoraDocument uuidFedoraDoc = fedoraCommunicator.getFedoraDocByUuid(uuid);
        if (uuidFedoraDoc == null) {
            problemFound = true;
            problems.add(new UuidProblem(notStoredProblemType));
        }

        return problemFound;
    }
}
