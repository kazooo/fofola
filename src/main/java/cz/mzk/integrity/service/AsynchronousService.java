package cz.mzk.integrity.service;

import cz.mzk.integrity.threads.SolrIntegrityCheckerThread;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

@Service
public class AsynchronousService {

    private final ThreadPoolExecutor threadPoolExecutor;

    private final SolrIntegrityCheckerThread solrIntegrityCheckerThread;

    public AsynchronousService(ThreadPoolExecutor threadPoolTaskExecutor,
                               SolrIntegrityCheckerThread solrIntegrityCheckerThread) {
        this.threadPoolExecutor = threadPoolTaskExecutor;
        this.solrIntegrityCheckerThread = solrIntegrityCheckerThread;
    }

    public void runSolrChecking(long processId, String model, long docCount) {
        solrIntegrityCheckerThread.setProcessId(processId);
        solrIntegrityCheckerThread.setModel(model);
        solrIntegrityCheckerThread.setDocCount(docCount);
        solrIntegrityCheckerThread.setCollectionName("kramerius");
        solrIntegrityCheckerThread.setDocsPerQuery(100);
        threadPoolExecutor.execute(solrIntegrityCheckerThread);
    }

    public void stopSolrChecking() {
        solrIntegrityCheckerThread.interrupt();
    }
    public boolean isCheckSolrProcessRunning() { return solrIntegrityCheckerThread.running(); }
    public long getCheckSolrStatusDone() { return solrIntegrityCheckerThread.getDone(); }
    public long getCheckSolrStatusTotal() { return solrIntegrityCheckerThread.getDocCount(); }
}
