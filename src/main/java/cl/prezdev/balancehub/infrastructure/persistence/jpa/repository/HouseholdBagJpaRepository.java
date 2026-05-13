package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.HouseholdBagJpaEntity;

public interface HouseholdBagJpaRepository extends JpaRepository<HouseholdBagJpaEntity, String> {
    List<HouseholdBagJpaEntity> findAllByOrderByNameAsc();

    Optional<HouseholdBagJpaEntity> findByName(String name);
}
