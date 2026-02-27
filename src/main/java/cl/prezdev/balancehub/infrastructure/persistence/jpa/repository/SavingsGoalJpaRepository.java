package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.SavingsGoalJpaEntity;

public interface SavingsGoalJpaRepository extends JpaRepository<SavingsGoalJpaEntity, String> {

    @Modifying
    @Transactional
    @Query("update SavingsGoalJpaEntity s set s.active = false where s.active = true")
    void deactivateCurrentSavingsGoal();

    Optional<SavingsGoalJpaEntity> findFirstByActiveTrue();
}
