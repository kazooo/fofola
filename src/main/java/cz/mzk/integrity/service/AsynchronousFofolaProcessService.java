package cz.mzk.integrity.service;

import cz.mzk.integrity.model.FofolaProcess;
import cz.mzk.integrity.repository.ProcessRepository;
import cz.mzk.integrity.threads.FofolaThreadEvent;
import cz.mzk.integrity.threads.SitemapGenerationThread;
import cz.mzk.integrity.threads.SolrIntegrityCheckerThread;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

@Service
public class AsynchronousFofolaProcessService {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final SolrIntegrityCheckerThread solrIntegrityCheckerThread;
    private final SitemapGenerationThread sitemapGenerationThread;
    private final ProcessRepository processRepository;

    private static final Logger logger = Logger.getLogger(AsynchronousFofolaProcessService.class.getName());

    public AsynchronousFofolaProcessService(ThreadPoolExecutor threadPoolTaskExecutor,
                                            SolrIntegrityCheckerThread solrIntegrityCheckerThread,
                                            SitemapGenerationThread sitemapGenerationThread,
                                            ProcessRepository processRepository) {
        this.threadPoolExecutor = threadPoolTaskExecutor;
        this.solrIntegrityCheckerThread = solrIntegrityCheckerThread;
        this.sitemapGenerationThread = sitemapGenerationThread;
        this.processRepository = processRepository;
    }

    @EventListener
    public void catchThreadFinishEvent(FofolaThreadEvent event) {

        switch (event.getType()) {

            case FofolaThreadEvent.CHECK_SOLR_FINISH:
                finishProcess(FofolaProcess.CHECK_SOLR_TYPE);
                logger.info("Solr integrity checking complete!");
                break;

            case FofolaThreadEvent.GEN_SITEMAPS_FINISH:
                finishProcess(FofolaProcess.GENERATE_SITEMAP_TYPE);
                logger.info("Sitemap generation complete!");
                break;

            case FofolaThreadEvent.CHECK_SOLR_EXC:
                finishProcess(FofolaProcess.CHECK_SOLR_TYPE);
                logger.severe("Solr integrity checking exception:");
                logger.severe(event.toString());
                break;

            case FofolaThreadEvent.GEN_SITEMAPS_EXC:
                finishProcess(FofolaProcess.GENERATE_SITEMAP_TYPE);
                logger.severe("Sitemap generation exception:");
                logger.severe(event.getMessage());
                break;
        }
    }

    public FofolaProcess getProcess(String type) {
        return processRepository.findFirstByProcessType(type);
    }

    private void finishProcess(String type) {
        FofolaProcess process = getProcess(type);
        process.finish();
        processRepository.saveAndFlush(process);
    }

    /*************** SOLR CHECKING ***************/

    public void runSolrChecking(String model, long docCount) {
        logger.info("Run Solr checking process.");

        FofolaProcess solrProcess = new FofolaProcess(FofolaProcess.CHECK_SOLR_TYPE);
        solrProcess = processRepository.save(solrProcess);

        solrIntegrityCheckerThread.setProcessId(solrProcess.getId());
        solrIntegrityCheckerThread.setModel(model);
        solrIntegrityCheckerThread.setDocCount(docCount);
        solrIntegrityCheckerThread.setCollectionName("kramerius");
        solrIntegrityCheckerThread.setDocsPerQuery(5000);
        threadPoolExecutor.execute(solrIntegrityCheckerThread);
    }

    public void stopSolrChecking() {
        logger.info("Stop Solr integrity checking.");
        solrIntegrityCheckerThread.interrupt();
        FofolaProcess process = getProcess(FofolaProcess.CHECK_SOLR_TYPE);
        process.stop();
        processRepository.saveAndFlush(process);
    }

    public void clearSolrChecking() {
        logger.info("Clear Solr integrity problem list.");
        FofolaProcess process = getProcess(FofolaProcess.CHECK_SOLR_TYPE);
        processRepository.delete(process);
    }

    public long getCheckSolrStatusDone() { return solrIntegrityCheckerThread.getDone(); }

    public long getCheckSolrStatusTotal() { return solrIntegrityCheckerThread.getDocCount(); }

    public String getCheckSolrModel() { return solrIntegrityCheckerThread.getModel(); }

    /*************** SITEMAP GENERATION ***************/

    public void runSitemapGenerationProcess(String outPath) {
        logger.info("Run sitemaps generation.");

        FofolaProcess generationProcess = new FofolaProcess(FofolaProcess.GENERATE_SITEMAP_TYPE);
        processRepository.save(generationProcess);

        sitemapGenerationThread.setCollectionName("kramerius");
        sitemapGenerationThread.setOutputDir(outPath);
        threadPoolExecutor.execute(sitemapGenerationThread);
    }

    public void stopSitemapGeneration() {
        logger.info("Stop sitemap generation process.");

        FofolaProcess generationProcess = getProcess(FofolaProcess.GENERATE_SITEMAP_TYPE);
        generationProcess.stop();
        processRepository.saveAndFlush(generationProcess);

        sitemapGenerationThread.interrupt();
    }

    public void clearSitemapGen() {
        logger.info("Clear generated sitemaps.");

        FofolaProcess generationProcess = getProcess(FofolaProcess.GENERATE_SITEMAP_TYPE);
        processRepository.delete(generationProcess);
    }

    public long getSitemapGenStatusDone() { return sitemapGenerationThread.getDone(); }

    public long getSitemapGenStatusTotal() { return sitemapGenerationThread.getTotal(); }

}
