package cl.prezdev.balancehub.domain.exception;

public class InvalidRecurringExpenseException extends RuntimeException {

    public InvalidRecurringExpenseException(String message) {
        super(message);
    }

}
