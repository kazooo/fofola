package cz.mzk.fofola.processes.core.events;

import java.util.Objects;

public class RemoveInfoEvent {

    private final String processId;

    public RemoveInfoEvent(String processId) {
        this.processId = processId;
    }

    public String getProcessId() {
        return processId;
    }

    @Override
    public String toString() {
        return "RemoveInfoEvent{" +
                "processId='" + processId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveInfoEvent that = (RemoveInfoEvent) o;
        return Objects.equals(processId, that.processId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId);
    }
}
