package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.prezdev.balancehub.domain.enums.ExpenseType;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.RecurringExpenseJpaEntity;

public interface RecurringExpenseJpaRepository extends JpaRepository<RecurringExpenseJpaEntity, String> {

    List<RecurringExpenseJpaEntity> findByTypeOrderByDescriptionAscIdAsc(ExpenseType type);

    @Query("select coalesce(sum(r.amount), 0) from RecurringExpenseJpaEntity r where r.type = :type")
    BigDecimal sumAmountByType(@Param("type") ExpenseType type);
}
