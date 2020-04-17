package cz.mzk.fofola.processes.core.constants;

public enum ProcessState {

    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    TERMINATED("Terminated"),
    FINISHED("Finished");

    private final String state;

    private ProcessState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
