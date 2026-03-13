package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.PendingRepository;
import cl.prezdev.balancehub.domain.Pending;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.PendingJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.PendingJpaRepository;

@Repository
public class PendingJpaAdapter implements PendingRepository {

    private final PendingJpaRepository repository;

    public PendingJpaAdapter(PendingJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Pending pending) {
        repository.save(toEntity(pending));
    }

    @Override
    public List<Pending> findAll() {
        return repository.findAllByOrderByCreatedAtAscIdAsc().stream()
            .map(this::toDomain)
            .toList();
    }

    @Override
    public Optional<Pending> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    private PendingJpaEntity toEntity(Pending pending) {
        return new PendingJpaEntity(pending.getId(), pending.getDescription(), pending.getCreatedAt());
    }

    private Pending toDomain(PendingJpaEntity entity) {
        return new Pending(entity.getId(), entity.getDescription(), entity.getCreatedAt());
    }
}
