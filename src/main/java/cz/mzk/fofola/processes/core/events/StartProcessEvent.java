package cz.mzk.fofola.processes.core.events;

import cz.mzk.fofola.processes.core.constants.ProcessType;

import java.util.Objects;

public class StartProcessEvent {

    private final String processId;
    private final ProcessType processType;

    public StartProcessEvent(String processId, ProcessType processType) {
        this.processId = processId;
        this.processType = processType;
    }

    public String getProcessId() {
        return processId;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    @Override
    public String toString() {
        return "StartProcessEvent{" +
                "processId='" + processId + '\'' +
                ", processType=" + processType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartProcessEvent that = (StartProcessEvent) o;
        return Objects.equals(processId, that.processId) &&
                processType == that.processType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, processType);
    }
}
