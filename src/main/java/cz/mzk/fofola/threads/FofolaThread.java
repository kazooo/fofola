package cz.mzk.fofola.threads;

import cz.mzk.fofola.model.FofolaProcess;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public abstract class FofolaThread implements Runnable {

    private String threadType;

    private static Map<String, String> finishEvent = new HashMap<String, String>() {
        {
            put(FofolaProcess.CHECK_SOLR_TYPE, FofolaThreadEvent.CHECK_SOLR_FINISH);
            put(FofolaProcess.GENERATE_SITEMAP_TYPE, FofolaThreadEvent.GEN_SITEMAPS_FINISH);
        }

    };

    private static Map<String, String> exceptionEvent = new HashMap<String, String>() {
        {
            put(FofolaProcess.CHECK_SOLR_TYPE, FofolaThreadEvent.CHECK_SOLR_EXC);
            put(FofolaProcess.GENERATE_SITEMAP_TYPE, FofolaThreadEvent.GEN_SITEMAPS_EXC);
        }

    };

    boolean keepProcess;
    private final FofolaThreadEventPublisher eventPublisher;

    protected FofolaThread(FofolaThreadEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void setType(String threadType) {
        this.threadType = threadType;
    }

    public void interrupt() {
        keepProcess = false;
    }

    public boolean running() {
        return keepProcess;
    }

    @Override
    public void run() {
        keepProcess = true;
        try {
            process();
        } catch (Exception e) {
            String exceptionDescStr = "Exception: " + e.toString() + "\n" + stackTraceToStr(e);
            eventPublisher.publishEvent(exceptionEvent.get(threadType), exceptionDescStr);
            return;
        } finally {
            keepProcess = false;
        }

        eventPublisher.publishEvent(finishEvent.get(threadType), "finished_successfully");
    }

    private String stackTraceToStr(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder stackTraceStr = new StringBuilder();
        for (StackTraceElement ste : stackTrace) {
            stackTraceStr.append(ste.toString());
            stackTraceStr.append("\n");
        }
        return stackTraceStr.toString();
    }

    protected abstract void process() throws Exception;
}
