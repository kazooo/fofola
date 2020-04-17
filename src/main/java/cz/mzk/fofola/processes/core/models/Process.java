package cz.mzk.fofola.processes.core.models;

import cz.mzk.fofola.processes.core.constants.FinishReason;
import cz.mzk.fofola.processes.core.events.TerminateProcessEvent;
import cz.mzk.fofola.processes.core.exceptions.FinishProcessException;
import org.axonframework.eventhandling.gateway.EventGateway;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public abstract class Process {

    private final String processId;
    private final EventGateway eventGateway;
    private static final String logDirPath = "logs/";
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Process(LinkedHashMap<String, Object> params) throws IOException {
        this.processId = (String) params.get("processId");
        this.eventGateway = (EventGateway) params.get("eventGateway");

        createLogDirIfDoesnExist();
        FileHandler fileHandler = new FileHandler(logDirPath + processId + ".log");
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);

        setupParams(params);
    }

    public void run() {
        try {
            process();
            eventGateway.publish(
                    new TerminateProcessEvent(processId, FinishReason.FINISH_SUCCESSFULLY, null)
            );
        } catch (FinishProcessException ignored) { } catch (Exception e) {
            String stackTraceStr = stackTraceToStr(e);
            logger.severe(stackTraceStr);
            eventGateway.publish(
                    new TerminateProcessEvent(processId, FinishReason.EXCEPTION, stackTraceStr)
            );
        }
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

    private void createLogDirIfDoesnExist() {
        File directory = new File(logDirPath);
        if (! directory.exists()){
            directory.mkdirs();
        }
    }

    public abstract void process() throws Exception;

    protected abstract void setupParams(LinkedHashMap<String, Object> params);
}
