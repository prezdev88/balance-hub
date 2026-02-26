package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.DebtJpaEntity;

public interface DebtJpaRepository extends JpaRepository<DebtJpaEntity, String> {

    List<DebtJpaEntity> findByDebtorIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDescIdAsc(
        String debtorId,
        Instant startInclusive,
        Instant endExclusive
    );

    @Query(
        value = """
            select coalesce(sum(i.amount), 0)
            from debts d
            join installments i on i.debt_id = d.id
            where d.debtor_id = :debtorId
              and i.paid_at is null
            """,
        nativeQuery = true
    )
    BigDecimal sumPendingAmountByDebtorId(@Param("debtorId") String debtorId);
}
