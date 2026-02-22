package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.DebtorJpaEntity;

public interface DebtorJpaRepository extends JpaRepository<DebtorJpaEntity, String> {
}
