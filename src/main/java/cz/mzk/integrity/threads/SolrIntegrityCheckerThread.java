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

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void process() {
        SimpleQuery query = new SimpleQuery(SolrDocument.MODEL + ":" + model)
                .addSort(Sort.by(SolrDocument.ID))
                .setRows(docsPerQuery);

        done = 0;
        List<UuidProblem> problems = new ArrayList<>();
        Cursor<SolrDocument> solrDocs = solrCommunicator.getDocCursor(collectionName, query);

        while (done < docCount && super.keepProcess && solrDocs.hasNext()) {
            SolrDocument solrDoc = solrDocs.next();
            String uuid = solrDoc.getUuid();

            FedoraDocument fedoraDoc = fedoraCommunicator.getFedoraDocByUuid(uuid);

            // check if stored in Fedora
            if (fedoraDoc == null) {
                problems.add(new UuidProblem(UuidProblem.NOT_STORED));
            }

            // check if doc in Fedora has the same visibility as doc in Solr
            if (fedoraDoc != null && !fedoraDoc.getAccesibility().equals(solrDoc.getAccessibility())) {
                problems.add(new UuidProblem(UuidProblem.DIFF_VISIBILITY));
            }

            // check doc root
            checkRootExistence(solrDoc.getRootPid(), problems);

            if (!problems.isEmpty()) {
                problemRepository.save(
                        new UuidProblemRecord(processId, uuid,
                                solrDoc.getRootTitle(), model,
                                problems));
                problems.clear();
            }
            done++;
        }
    }

    private void checkRootExistence(String uuid, List<UuidProblem> problems) {

        // check if uuid exists
        if (uuid == null || uuid.isEmpty()) {
            problems.add(new UuidProblem(UuidProblem.NO_ROOT));
            return;
        }

        // check if uuid indexed
        SolrDocument uuidSolrDoc = solrCommunicator.getSolrDocByUuid(uuid);
        if (uuidSolrDoc == null) {
            problems.add(new UuidProblem(UuidProblem.ROOT_NOT_INDEXED));
        }

        // check if uuid stored
        FedoraDocument uuidFedoraDoc = fedoraCommunicator.getFedoraDocByUuid(uuid);
        if (uuidFedoraDoc == null) {
            problems.add(new UuidProblem(UuidProblem.ROOT_NOT_STORED));
        }
    }
}
