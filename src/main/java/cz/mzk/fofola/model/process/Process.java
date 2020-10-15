package cz.mzk.fofola.model.process;

import cz.mzk.fofola.process.FinishProcessException;
import cz.mzk.fofola.process.ProcessEventNotifier;
import cz.mzk.fofola.process.utils.FileUtils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public abstract class Process {

    private final FileHandler fileHandler;
    private final ProcessParams params;
    private final ProcessEventNotifier eventNotifier;
    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    protected Process(ProcessParams params) throws IOException {
        this.params = params;
        eventNotifier = params.getEventNotifier();
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler = FileUtils.getLogFileHandler(params.getId() + ".log");
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
    }

    public void run() {
        eventNotifier.notifyStart(params.getId(), params.getType());
        try {
            TerminationReason reason = process();
            logger.info("Process job is successfully finished!");
            eventNotifier.notifyFinish(params.getId(), reason, null);
        } catch (FinishProcessException ignored) { } catch (Throwable e) {
            String stackTraceStr = stackTraceToStr(e);
            logger.severe("Process job is has been terminated by exception!");
            logger.severe(stackTraceStr);
            eventNotifier.notifyFinish(params.getId(), TerminationReason.EXCEPTION, stackTraceStr);
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

    public abstract TerminationReason process() throws Exception;
}
