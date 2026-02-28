package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.prezdev.balancehub.domain.enums.UserRole;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.AuthUserJpaEntity;

public interface AuthUserJpaRepository extends JpaRepository<AuthUserJpaEntity, String> {
    Optional<AuthUserJpaEntity> findByEmailIgnoreCase(String email);

    Optional<AuthUserJpaEntity> findByDebtorId(String debtorId);

    boolean existsByDebtorIdAndEnabledTrue(String debtorId);

    boolean existsByRole(UserRole role);
}
