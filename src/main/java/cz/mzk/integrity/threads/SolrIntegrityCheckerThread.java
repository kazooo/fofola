package cz.mzk.integrity.threads;

import cz.mzk.integrity.model.FedoraDocument;
import cz.mzk.integrity.model.SolrDocument;
import cz.mzk.integrity.model.UuidProblem;
import cz.mzk.integrity.repository.ProblemRepository;
import cz.mzk.integrity.repository.ProcessRepository;
import cz.mzk.integrity.service.FedoraCommunicator;
import cz.mzk.integrity.service.SolrCommunicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SolrIntegrityCheckerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SolrIntegrityCheckerThread.class);

    private String model;
    private long docCount;
    private int docsPerQuery;
    private String collectionName;
    private long processId;

    private final SolrCommunicator solrCommunicator;
    private final FedoraCommunicator fedoraCommunicator;
    private final ProblemRepository problemRepository;
    private final ProcessRepository processRepository;

    public SolrIntegrityCheckerThread(SolrCommunicator solrCommunicator,
                                      FedoraCommunicator fedoraCommunicator,
                                      ProblemRepository problemRepository,
                                      ProcessRepository processRepository) {
        this.solrCommunicator = solrCommunicator;
        this.fedoraCommunicator = fedoraCommunicator;
        this.problemRepository = problemRepository;
        this.processRepository = processRepository;
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

    public synchronized void interrupt() {
        if (Thread.currentThread().isAlive() && !Thread.currentThread().isInterrupted()) {
            logger.info("stopping solr integrity checking...");
            Thread.currentThread().interrupt();
            processRepository.deleteById(processId);
        }
    }

    @Override
    public void run() {
        logger.info("run solr integrity checking...");

        SimpleQuery query = new SimpleQuery(SolrDocument.MODEL + ":" + model)
                .addSort(Sort.by(SolrDocument.ID)).setRows(docsPerQuery);

        long done = 0;
        List<SolrDocument> solrDocs;

        while (done < docCount) {
            solrDocs = solrCommunicator.cursorQuery(collectionName, query);
            done += solrDocs.size();

            for (SolrDocument solrDoc : solrDocs) {
                String uuid = solrDoc.getUuid();

                FedoraDocument fedoraDoc = fedoraCommunicator.getFedoraDocByUuid(uuid);

                if (fedoraDoc == null) {
//                    logger.info(uuid + " -> not stored!");
                    problemRepository.save(new UuidProblem(processId, uuid, UuidProblem.NOT_STORED));
                }

            }
        }

        logger.info("done solr integrity checking...");
        processRepository.deleteById(processId);
    }
}
