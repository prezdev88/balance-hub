package cl.prezdev.balancehub.application.ports.out;

import java.util.List;
import java.util.Optional;

import cl.prezdev.balancehub.domain.Pending;

public interface PendingRepository {
    void save(Pending pending);

    List<Pending> findAll();

    Optional<Pending> findById(String id);

    void deleteById(String id);
}
