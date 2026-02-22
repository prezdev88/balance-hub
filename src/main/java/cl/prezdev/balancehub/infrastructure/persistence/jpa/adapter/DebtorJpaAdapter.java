package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.domain.Debtor;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.DebtorJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.DebtorJpaRepository;

@Repository
public class DebtorJpaAdapter implements DebtorRepository {

    private final DebtorJpaRepository repository;

    public DebtorJpaAdapter(DebtorJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Debtor debtor) {
        repository.save(toEntity(debtor));
    }

    @Override
    public List<Debtor> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Debtor> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    private DebtorJpaEntity toEntity(Debtor debtor) {
        return new DebtorJpaEntity(debtor.getId(), debtor.getName(), debtor.getEmail());
    }

    private Debtor toDomain(DebtorJpaEntity entity) {
        return new Debtor(entity.getId(), entity.getName(), entity.getEmail());
    }
}
