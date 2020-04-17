package cz.mzk.fofola.processes.core.exceptions;

public class FinishProcessException extends Exception {

    public FinishProcessException(Exception e) {
        super("Process has been finished by incoming interruption", e);
    }
}
