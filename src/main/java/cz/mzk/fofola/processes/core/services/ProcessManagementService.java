package cz.mzk.fofola.processes.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import cz.mzk.fofola.processes.core.models.Process;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


@Service
@Slf4j
public class ProcessManagementService {

    private final ThreadPoolExecutor threadPoolExecutor;

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
            log.info("Terminating process " + processId + "...");
            thread.interrupt();
            log.info("Process " + processId + " is terminated!");
        }
    }

    private Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }
}
