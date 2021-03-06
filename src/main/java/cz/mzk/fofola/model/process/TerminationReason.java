package cz.mzk.fofola.model.process;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum TerminationReason {

    EXCEPTION("Exception occurred"),
    USER_COMMAND("User terminates the process");

    private final String reason;
}
