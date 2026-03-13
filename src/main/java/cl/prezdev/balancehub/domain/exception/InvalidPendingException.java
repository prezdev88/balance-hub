package cl.prezdev.balancehub.domain.exception;

public class InvalidPendingException extends RuntimeException {

    public InvalidPendingException(String message) {
        super(message);
    }
}
