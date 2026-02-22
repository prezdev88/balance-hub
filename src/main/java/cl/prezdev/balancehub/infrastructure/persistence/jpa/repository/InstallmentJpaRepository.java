package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.InstallmentJpaEntity;

public interface InstallmentJpaRepository extends JpaRepository<InstallmentJpaEntity, String> {

    List<InstallmentJpaEntity> findByDebtIdInOrderByDebtIdAscInstallmentNoAsc(List<String> debtIds);
}
