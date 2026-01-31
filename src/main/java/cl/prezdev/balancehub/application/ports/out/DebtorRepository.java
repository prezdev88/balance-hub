package cl.prezdev.balancehub.application.ports.out;

import cl.prezdev.balancehub.domain.Debtor;

public interface DebtorRepository {
    void save(Debtor debtor);
}
