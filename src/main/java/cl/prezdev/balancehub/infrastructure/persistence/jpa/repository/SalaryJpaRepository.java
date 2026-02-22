package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.SalaryJpaEntity;

public interface SalaryJpaRepository extends JpaRepository<SalaryJpaEntity, String> {

    @Modifying
    @Transactional
    @Query("update SalaryJpaEntity s set s.active = false where s.active = true")
    void deactivateCurrentSalary();
}
