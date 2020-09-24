package cz.mzk.fofola.processes.core.events;

import java.util.Objects;

public class FinishProcessEvent {

    private final String processId;

    public FinishProcessEvent(String processId) {
        this.processId = processId;
    }

    public String getProcessId() {
        return processId;
    }

    @Override
    public String toString() {
        return "TerminateProcessEvent{" +
                "processId='" + processId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinishProcessEvent that = (FinishProcessEvent) o;
        return Objects.equals(processId, that.processId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId);
    }
}
