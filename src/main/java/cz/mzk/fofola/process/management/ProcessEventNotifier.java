package cz.mzk.fofola.process.management;

import cz.mzk.fofola.process.constants.ProcessState;
import cz.mzk.fofola.process.constants.ProcessType;
import cz.mzk.fofola.process.constants.TerminationReason;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@Slf4j
@AllArgsConstructor
public class ProcessEventNotifier {

    private final FProcessRepository processRepository;

    public void notifyStart(String id, ProcessType type) {
        log.info("Got start notification for process with id " + id);
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setId(id);
        processDTO.setType(type);
        processDTO.setState(ProcessState.ACTIVE);
        processDTO.setStart(new Date());
        processRepository.save(processDTO);
    }

    public void notifyFinish(String id, TerminationReason reason, String note) {
        log.info("Got finish notification for process with id " + id);
        ProcessDTO processDTO = processRepository.findById(id).get();
        ProcessState state = getStateByFinishReason(reason);
        processDTO.setState(state);
        processDTO.setFinish(new Date());
        processDTO.setNotes(note);
        processDTO.setTerminationReason(reason);
        processRepository.save(processDTO);
    }

    private ProcessState getStateByFinishReason(TerminationReason reason) {
        if (reason == TerminationReason.EXCEPTION) return ProcessState.TERMINATED;
        else return ProcessState.FINISHED;
    }
}
