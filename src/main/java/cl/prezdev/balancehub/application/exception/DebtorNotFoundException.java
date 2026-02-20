package cl.prezdev.balancehub.application.exception;

public class DebtorNotFoundException extends RuntimeException {
    public DebtorNotFoundException(String message) {
        super(message);
    }

}
