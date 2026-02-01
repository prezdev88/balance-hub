package cl.prezdev.balancehub.application.ports.out;

import java.util.List;

import cl.prezdev.balancehub.domain.Debtor;

public interface DebtorRepository {
    void save(Debtor debtor);

    List<Debtor> findAll();
}
