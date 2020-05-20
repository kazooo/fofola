package cz.mzk.fofola.processes.core.services;

import org.springframework.stereotype.Service;
import cz.mzk.fofola.processes.core.models.Process;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

@Service
public class ProcessManagementService {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ProcessManagementService() {
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    }

    public void run(String processId, Process process) {
        threadPoolExecutor.execute(() -> {
            Thread.currentThread().setName(processId);
            process.run();
        });
    }

    public void terminate(String processId) {
        Thread thread = getThreadByName(processId);
        if (thread != null) {
            logger.info("Terminating process " + processId + "...");
            thread.interrupt();
            logger.info("Process " + processId + " is terminated!");
        }
    }

    private Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }
}
