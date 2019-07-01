package cz.mzk.integrity.service;

import cz.mzk.integrity.solr_integrity.SolrIntegrityCheckerThread;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class AsynchronousService {

    private final TaskExecutor taskExecutor;

    private final ApplicationContext applicationContext;

    public AsynchronousService(TaskExecutor taskExecutor,
                               ApplicationContext applicationContext) {
        this.taskExecutor = taskExecutor;
        this.applicationContext = applicationContext;
    }

    public void runSolrChecking(String model, long docCount) {

        SolrIntegrityCheckerThread thread =
                applicationContext.getBean(SolrIntegrityCheckerThread.class);
        thread.setModel(model);
        thread.setDocCount(docCount);
        thread.setCollectionName("kramerius");
        thread.setDocsPerQuery(100);
        taskExecutor.execute(thread);
    }
}
