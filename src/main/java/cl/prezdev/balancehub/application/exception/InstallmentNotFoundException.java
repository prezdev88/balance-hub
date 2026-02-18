package cl.prezdev.balancehub.application.exception;

public class InstallmentNotFoundException extends RuntimeException {

    public InstallmentNotFoundException(String message) {
        super(message);
    }

}
