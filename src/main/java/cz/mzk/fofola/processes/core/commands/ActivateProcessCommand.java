package cz.mzk.fofola.processes.core.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

public class ActivateProcessCommand {

    @TargetAggregateIdentifier
    private final String processId;

    public ActivateProcessCommand(String processId) {
        this.processId = processId;
    }

    public String getProcessId() {
        return processId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivateProcessCommand that = (ActivateProcessCommand) o;
        return Objects.equals(processId, that.processId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId);
    }

    @Override
    public String toString() {
        return "ActivateProcessCommand{" +
                "processId='" + processId + '\'' +
                '}';
    }
}
