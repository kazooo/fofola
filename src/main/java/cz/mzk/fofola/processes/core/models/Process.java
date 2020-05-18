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
    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    public Process(LinkedHashMap<String, Object> params) throws IOException {
        this.processId = (String) params.get("processId");
        this.eventGateway = (EventGateway) params.get("eventGateway");

        createLogDirIfDoesnExist();
        FileHandler fileHandler = new FileHandler(logDirPath + processId + ".log");
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
    }

    public void run() {
        try {
            process();
            eventGateway.publish(
                    new TerminateProcessEvent(processId, FinishReason.FINISH_SUCCESSFULLY, null)
            );
        } catch (FinishProcessException ignored) { } catch (Throwable e) {
            String stackTraceStr = stackTraceToStr(e);
            logger.severe(stackTraceStr);
            eventGateway.publish(
                    new TerminateProcessEvent(processId, FinishReason.EXCEPTION, stackTraceStr)
            );
        }
    }

    private String stackTraceToStr(Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder stackTraceStr = new StringBuilder(e.getMessage() + "\n");
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
}
