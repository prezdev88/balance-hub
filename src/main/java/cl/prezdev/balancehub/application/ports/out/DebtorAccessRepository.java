package cl.prezdev.balancehub.application.ports.out;

public interface DebtorAccessRepository {
    boolean canLogin(String debtorId);
}
