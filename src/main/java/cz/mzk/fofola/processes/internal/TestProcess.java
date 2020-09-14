package cz.mzk.fofola.processes.internal;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.processes.core.models.Process;

import java.io.IOException;
import java.util.LinkedHashMap;


public class TestProcess extends Process {

    public TestProcess(LinkedHashMap<String, Object> params,
                       FofolaConfiguration fofolaConfiguration) throws IOException {
        super(params);
    }

    @Override
    public void process() {
        logger.info("test process invocation");
        for (int i = 0; i < 1000000000; i++) {
            logger.info("" + i);
            if (Thread.interrupted()) {
                logger.info("test process has been interupted!");
                return;
            }
        }
        logger.info("test process finish");
    }
}
