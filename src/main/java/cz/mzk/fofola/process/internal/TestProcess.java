package cz.mzk.fofola.process.internal;

import java.io.IOException;
import cz.mzk.fofola.process.management.Process;
import cz.mzk.fofola.process.management.ProcessParams;
import cz.mzk.fofola.process.constants.TerminationReason;


public class TestProcess extends Process {

    public TestProcess(ProcessParams params) throws IOException {
        super(params);
    }

    @Override
    public TerminationReason process() {
        logger.info("test process invocation");
        for (int i = 0; i < 1000000000; i++) {
            logger.info("" + i);
            if (Thread.interrupted()) {
                logger.info("test process has been interupted!");
                return TerminationReason.USER_COMMAND;
            }
        }
        logger.info("test process finish");
        return null;
    }
}
