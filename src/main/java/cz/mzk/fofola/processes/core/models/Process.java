package cz.mzk.fofola.processes.core.models;

import cz.mzk.fofola.processes.core.constants.TerminationReason;
import cz.mzk.fofola.processes.core.events.FinishProcessEvent;
import cz.mzk.fofola.processes.core.events.TerminateProcessEvent;
import cz.mzk.fofola.processes.core.exceptions.FinishProcessException;
import cz.mzk.fofola.processes.utils.FileUtils;
import org.axonframework.eventhandling.gateway.EventGateway;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public abstract class Process {

    private final String processId;
    private final EventGateway eventGateway;
    private final FileHandler fileHandler;
    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    public Process(LinkedHashMap<String, Object> params) throws IOException {
        this.processId = (String) params.get("processId");
        this.eventGateway = (EventGateway) params.get("eventGateway");

        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler = FileUtils.getLogFileHandler(processId + ".log");
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
    }

    public void run() {
        try {
            process();
            logger.info("Process job is successfully finished!");
            eventGateway.publish(
                    new FinishProcessEvent(processId)
            );
        } catch (FinishProcessException ignored) { } catch (Throwable e) {
            String stackTraceStr = stackTraceToStr(e);
            logger.severe("Process job is has been terminated by exception!");
            logger.severe(stackTraceStr);
            eventGateway.publish(
                    new TerminateProcessEvent(processId, TerminationReason.EXCEPTION, stackTraceStr)
            );
        } finally {
            fileHandler.close();
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

    public abstract void process() throws Exception;
}
