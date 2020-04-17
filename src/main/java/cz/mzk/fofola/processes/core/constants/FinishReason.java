package cz.mzk.fofola.processes.core.constants;

public enum FinishReason {

    EXCEPTION("Exception occurred"),
    FINISH_SUCCESSFULLY("Finish successfully"),
    USER_COMMAND("User terminates the process");

    private final String reason;

    private FinishReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
