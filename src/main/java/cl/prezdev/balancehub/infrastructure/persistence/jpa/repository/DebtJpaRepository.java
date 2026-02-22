package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.DebtJpaEntity;

public interface DebtJpaRepository extends JpaRepository<DebtJpaEntity, String> {

    List<DebtJpaEntity> findByDebtorIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDescIdAsc(
        String debtorId,
        Instant startInclusive,
        Instant endExclusive
    );
}
