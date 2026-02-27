package cl.prezdev.balancehub.domain.exception;

public class InvalidSavingsGoalException extends RuntimeException {
    public InvalidSavingsGoalException(String message) {
        super(message);
    }
}
