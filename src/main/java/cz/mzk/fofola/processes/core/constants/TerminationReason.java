package cz.mzk.fofola.processes.core.constants;

public enum TerminationReason {

    EXCEPTION("Exception occurred"),
    USER_COMMAND("User terminates the process");

    private final String reason;

    private TerminationReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
