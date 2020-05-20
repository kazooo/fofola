package cz.mzk.fofola.processes.core;

import cz.mzk.fofola.processes.core.constants.ProcessState;
import cz.mzk.fofola.processes.core.events.*;
import cz.mzk.fofola.processes.core.models.ProcessDTO;
import cz.mzk.fofola.processes.core.queries.FindAllProcessQuery;
import cz.mzk.fofola.processes.core.services.ProcessRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProcessProjection {

    private final Logger logger = Logger.getLogger(ProcessProjection.class.getName());
    private final ProcessRepository processRepository;

    public ProcessProjection(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @EventHandler
    public void on(StartProcessEvent event) {
        ProcessDTO process = new ProcessDTO();
        process.setProcessId(event.getProcessId());
        process.setProcessType(event.getProcessType());
        process.setProcessState(ProcessState.ACTIVE);
        process.setStartDate(new Date());
        processRepository.save(process);
        logger.info("New process started: " + process.getProcessId());
    }

    @EventHandler
    public void on(TerminateProcessEvent event) {
        ProcessDTO process = processRepository.getOne(event.getProcessId());
        if (process.getProcessState() == ProcessState.FINISHED ||
                process.getProcessState() == ProcessState.TERMINATED) {
            logger.info("Process " + process.getProcessId() + " is already terminated in database!");
            return;
        }
        process.setProcessState(ProcessState.TERMINATED);
        process.setFinishDate(new Date());
        process.setFinishReason(event.getFinishReason());
        processRepository.save(process);
        logger.info("Process " + process.getProcessId() + " has been terminated in database!");
    }

    @EventHandler
    public void on(RemoveInfoEvent event) {
        processRepository.deleteById(event.getProcessId());
        logger.info("Remove process info: " + event.getProcessId());
    }

    @QueryHandler
    public List<ProcessDTO> handle(FindAllProcessQuery query) {
        return processRepository.findAll();
    }
}
