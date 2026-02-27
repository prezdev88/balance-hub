package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.SavingsGoalRepository;
import cl.prezdev.balancehub.domain.SavingsGoal;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.SavingsGoalJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.SavingsGoalJpaRepository;

@Repository
public class SavingsGoalJpaAdapter implements SavingsGoalRepository {

    private final SavingsGoalJpaRepository repository;

    public SavingsGoalJpaAdapter(SavingsGoalJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(SavingsGoal savingsGoal) {
        repository.save(new SavingsGoalJpaEntity(
            savingsGoal.getId(),
            savingsGoal.getAmount(),
            savingsGoal.getCreatedAt(),
            savingsGoal.isActive()
        ));
    }

    @Override
    public void deactivateCurrentSavingsGoal() {
        repository.deactivateCurrentSavingsGoal();
    }

    @Override
    public Optional<SavingsGoal> findActive() {
        return repository.findFirstByActiveTrue().map(this::toDomain);
    }

    private SavingsGoal toDomain(SavingsGoalJpaEntity entity) {
        return new SavingsGoal(entity.getId(), entity.getAmount(), entity.getCreatedAt(), entity.isActive());
    }
}
