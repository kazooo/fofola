package cz.mzk.fofola.processes.core.exceptions;

public class UnexpectedProcessException extends Exception {

    public UnexpectedProcessException(Exception e) {
        super("Process has been interrupted by unexpected exception", e);
    }
}
