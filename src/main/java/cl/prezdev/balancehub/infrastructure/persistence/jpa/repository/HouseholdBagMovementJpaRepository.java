package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.HouseholdBagMovementJpaEntity;

public interface HouseholdBagMovementJpaRepository extends JpaRepository<HouseholdBagMovementJpaEntity, String> {
    List<HouseholdBagMovementJpaEntity> findAllByBagIdOrderByCreatedAtDesc(String bagId);
}
