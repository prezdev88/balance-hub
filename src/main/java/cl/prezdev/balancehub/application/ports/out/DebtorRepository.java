package cl.prezdev.balancehub.application.ports.out;

import java.util.List;
import java.util.Optional;

import cl.prezdev.balancehub.domain.Debtor;

public interface DebtorRepository {
    void save(Debtor debtor);

    List<Debtor> findAll();

    Optional<Debtor> findById(String id);
}
