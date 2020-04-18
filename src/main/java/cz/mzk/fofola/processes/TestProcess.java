package cz.mzk.fofola.processes;

import cz.mzk.fofola.processes.core.exceptions.FinishProcessException;
import cz.mzk.fofola.processes.core.models.Process;

import java.io.IOException;
import java.util.LinkedHashMap;


public class TestProcess extends Process {

    public TestProcess(LinkedHashMap<String, Object> params) throws IOException {
        super(params);
    }

    @Override
    protected void setupParams(LinkedHashMap<String, Object> params) { }

    @Override
    public void process() throws Exception {
        logger.info("invocation");
        try {
            for (int i = 0; i < 10; i++) {
                logger.info("" + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            logger.info("catch interrupted exception " + Thread.currentThread().getId());
            throw new FinishProcessException(e);
        }
        logger.info("finish");
    }
}
