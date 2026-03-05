package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.HouseholdBudgetConfigJpaEntity;

public interface HouseholdBudgetConfigJpaRepository extends JpaRepository<HouseholdBudgetConfigJpaEntity, HouseholdBudgetCategory> {}
