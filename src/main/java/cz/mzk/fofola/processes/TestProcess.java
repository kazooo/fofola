package cz.mzk.fofola.processes;

import cz.mzk.fofola.configuration.FofolaConfiguration;
import cz.mzk.fofola.processes.core.exceptions.FinishProcessException;
import cz.mzk.fofola.processes.core.models.Process;

import java.io.IOException;
import java.util.LinkedHashMap;


public class TestProcess extends Process {

    public TestProcess(LinkedHashMap<String, Object> params,
                       FofolaConfiguration fofolaConfiguration) throws IOException {
        super(params);
    }

    @Override
    public void process() throws Exception {
        logger.info("test process invocation");
        try {
            for (int i = 0; i < 10; i++) {
                logger.info("" + i);
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            logger.info("catch interrupted exception");
            throw new FinishProcessException(e);
        }
        logger.info("test process finish");
    }
}
