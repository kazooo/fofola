package cz.mzk.fofola.processes.core.models;

import cz.mzk.fofola.processes.core.constants.FinishReason;
import cz.mzk.fofola.processes.core.constants.ProcessState;
import cz.mzk.fofola.processes.core.constants.ProcessType;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Entity
public class ProcessDTO {

    @Id
    private String processId;
    private ProcessType processType;
    private ProcessState processState;
    private Date startDate;
    private Date finishDate;
    private FinishReason finishReason;
    private String notes;

    public String getProcessId() {
        return processId;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public ProcessState getProcessState() {
        return processState;
    }

    public String getDescription() {
        return processType.getDescription();
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public FinishReason getFinishReason() {
        return finishReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public void setProcessState(ProcessState processState) {
        this.processState = processState;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public void setFinishReason(FinishReason finishReason) {
        this.finishReason = finishReason;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ProcessDTO{" +
                "processId='" + processId + '\'' +
                ", processType=" + processType +
                ", processState=" + processState +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", finishReason=" + finishReason +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessDTO that = (ProcessDTO) o;
        return Objects.equals(processId, that.processId) &&
                processType == that.processType &&
                processState == that.processState &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(finishDate, that.finishDate) &&
                finishReason == that.finishReason &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, processType, processState,
                startDate, finishDate, finishReason, notes);
    }
}
