package cz.mzk.fofola.processes.core.events;

import java.util.Objects;

public class ActivateProcessEvent {

    private final String processId;

    public ActivateProcessEvent(String processId) {
        this.processId = processId;
    }

    public String getProcessId() {
        return processId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivateProcessEvent that = (ActivateProcessEvent) o;
        return Objects.equals(processId, that.processId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId);
    }

    @Override
    public String toString() {
        return "ActivateProcessEvent{" +
                "processId='" + processId + '\'' +
                '}';
    }
}
