package cz.mzk.fofola.processes.core;

import cz.mzk.fofola.processes.core.constants.ProcessState;
import cz.mzk.fofola.processes.core.events.ActivateProcessEvent;
import cz.mzk.fofola.processes.core.events.StartProcessEvent;
import cz.mzk.fofola.processes.core.events.SuspendProcessEvent;
import cz.mzk.fofola.processes.core.events.TerminateProcessEvent;
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
        logger.info("new process started: " + process.getProcessId());
    }

    @EventHandler
    public void on(SuspendProcessEvent event) {
        ProcessDTO process = processRepository.getOne(event.getProcessId());
        process.setProcessState(ProcessState.SUSPENDED);
        process.setNotes(process.getNotes() + "\n\nSuspended at " + new Date().toString());
        processRepository.save(process);
    }

    @EventHandler
    public void on(ActivateProcessEvent event) {
        ProcessDTO process = processRepository.getOne(event.getProcessId());
        process.setProcessState(ProcessState.ACTIVE);
        process.setNotes(process.getNotes() + "\n\nActivated at " + new Date().toString());
        processRepository.save(process);
    }

    @EventHandler
    public void on(TerminateProcessEvent event) {
        ProcessDTO process = processRepository.getOne(event.getProcessId());
        process.setProcessState(ProcessState.TERMINATED);
        process.setFinishDate(new Date());
        process.setFinishReason(event.getFinishReason());
        processRepository.save(process);
        logger.info("new process finished: " + process.getProcessId());
    }

    @QueryHandler
    public List<ProcessDTO> handle(FindAllProcessQuery query) {
        return processRepository.findAll();
    }
}
