package cl.prezdev.balancehub.application.exception;

public class PendingNotFoundException extends RuntimeException {

    public PendingNotFoundException(String message) {
        super(message);
    }
}
