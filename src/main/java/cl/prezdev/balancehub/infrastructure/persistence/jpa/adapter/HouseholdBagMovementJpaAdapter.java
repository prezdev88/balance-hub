package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.HouseholdBagMovementRepository;
import cl.prezdev.balancehub.domain.HouseholdBagMovement;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.HouseholdBagMovementJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.HouseholdBagMovementJpaRepository;

@Repository
public class HouseholdBagMovementJpaAdapter implements HouseholdBagMovementRepository {

    private final HouseholdBagMovementJpaRepository repository;

    public HouseholdBagMovementJpaAdapter(HouseholdBagMovementJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(HouseholdBagMovement movement) {
        HouseholdBagMovementJpaEntity entity = toEntity(movement);
        repository.save(entity);
    }

    @Override
    public List<HouseholdBagMovement> findByBagId(String bagId) {
        return repository.findAllByBagIdOrderByCreatedAtDesc(bagId).stream()
            .map(this::toDomain)
            .toList();
    }

    private HouseholdBagMovementJpaEntity toEntity(HouseholdBagMovement movement) {
        return new HouseholdBagMovementJpaEntity(
            movement.getId(),
            movement.getBagId(),
            movement.getAmount(),
            movement.getMovementType(),
            movement.getCreatedAt()
        );
    }

    private HouseholdBagMovement toDomain(HouseholdBagMovementJpaEntity entity) {
        return new HouseholdBagMovement(
            entity.getId(),
            entity.getBagId(),
            entity.getAmount(),
            entity.getMovementType(),
            entity.getCreatedAt()
        );
    }
}
