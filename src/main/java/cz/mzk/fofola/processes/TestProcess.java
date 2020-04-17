package cz.mzk.fofola.processes;

import cz.mzk.fofola.processes.core.exceptions.FinishProcessException;
import cz.mzk.fofola.processes.core.models.Process;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class TestProcess extends Process {

    private final Logger logger = Logger.getLogger(TestProcess.class.getName());

    public TestProcess(HashMap<String, Object> params) throws IOException {
        super(params);
    }

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

    @Override
    protected void setupParams(HashMap<String, Object> params) {

    }
}
