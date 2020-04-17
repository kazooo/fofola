package cz.mzk.fofola.processes.core.events;

import cz.mzk.fofola.processes.core.constants.FinishReason;

import java.util.Objects;


public class TerminateProcessEvent {

    private final String processId;
    private FinishReason finishReason;
    private String note;

    public TerminateProcessEvent(String processId, FinishReason finishReason, String note) {
        this.processId = processId;
        this.finishReason = finishReason;
        this.note = note;
    }

    public String getProcessId() {
        return processId;
    }

    public FinishReason getFinishReason() {
        return finishReason;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "TerminateProcessEvent{" +
                "processId='" + processId + '\'' +
                ", finishReason=" + finishReason +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminateProcessEvent that = (TerminateProcessEvent) o;
        return Objects.equals(processId, that.processId) &&
                finishReason == that.finishReason &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, finishReason, note);
    }
}
