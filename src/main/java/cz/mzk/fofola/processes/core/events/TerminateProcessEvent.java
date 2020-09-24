package cz.mzk.fofola.processes.core.events;

import cz.mzk.fofola.processes.core.constants.TerminationReason;

import java.util.Objects;


public class TerminateProcessEvent {

    private final String processId;
    private final TerminationReason reason;
    private final String note;

    public TerminateProcessEvent(String processId, TerminationReason terminationReason, String note) {
        this.processId = processId;
        this.reason = terminationReason;
        this.note = note;
    }

    public String getProcessId() {
        return processId;
    }

    public TerminationReason getReason() {
        return reason;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "TerminateProcessEvent{" +
                "processId='" + processId + '\'' +
                ", finishReason=" + reason +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminateProcessEvent that = (TerminateProcessEvent) o;
        return Objects.equals(processId, that.processId) &&
                reason == that.reason &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, reason, note);
    }
}
