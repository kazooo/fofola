package cz.mzk.fofola.process;

public class FinishProcessException extends Exception {

    public FinishProcessException(Exception e) {
        super("Process has been finished by incoming interruption", e);
    }
}
