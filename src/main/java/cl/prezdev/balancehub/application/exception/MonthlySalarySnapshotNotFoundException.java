package cl.prezdev.balancehub.application.exception;

public class MonthlySalarySnapshotNotFoundException extends RuntimeException {
    public MonthlySalarySnapshotNotFoundException(String message) {
        super(message);
    }
}
