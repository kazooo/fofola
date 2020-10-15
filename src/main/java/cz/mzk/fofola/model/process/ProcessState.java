package cz.mzk.fofola.model.process;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ProcessState {

    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    TERMINATED("Terminated"),
    FINISHED("Finished");

    private final String state;
}
