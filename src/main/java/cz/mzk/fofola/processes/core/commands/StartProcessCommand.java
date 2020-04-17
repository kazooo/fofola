package cz.mzk.fofola.processes.core.commands;

import cz.mzk.fofola.processes.core.constants.ProcessType;
import cz.mzk.fofola.processes.core.models.Process;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

public class StartProcessCommand {

    @TargetAggregateIdentifier
    private final String processId;
    private final ProcessType processType;
    private final Process process;

    public StartProcessCommand(String processId, ProcessType processType, Process process) {
        this.processId = processId;
        this.processType = processType;
        this.process = process;
    }

    public String getProcessId() {
        return processId;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public Process getProcess() {
        return process;
    }

    @Override
    public String toString() {
        return "StartProcessCommand{" +
                "processId='" + processId + '\'' +
                ", processType=" + processType +
                ", process=" + process +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartProcessCommand that = (StartProcessCommand) o;
        return Objects.equals(processId, that.processId) &&
                processType == that.processType &&
                Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, processType, process);
    }
}
