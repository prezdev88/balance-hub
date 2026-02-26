package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.domain.RecurringExpense;
import cl.prezdev.balancehub.domain.enums.ExpenseType;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.RecurringExpenseJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.RecurringExpenseJpaRepository;

@Repository
public class RecurringExpenseJpaAdapter implements RecurringExpenseRepository {

    private final RecurringExpenseJpaRepository repository;

    public RecurringExpenseJpaAdapter(RecurringExpenseJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(RecurringExpense recurringExpense) {
        repository.save(toEntity(recurringExpense));
    }

    @Override
    public void update(RecurringExpense recurringExpense) {
        repository.save(toEntity(recurringExpense));
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<RecurringExpense> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<RecurringExpense> findByType(ExpenseType type) {
        return repository.findByTypeOrderByDescriptionAscIdAsc(type)
            .stream()
            .map(this::toDomain)
            .toList();
    }

    @Override
    public BigDecimal totalByType(ExpenseType type) {
        BigDecimal total = repository.sumAmountByType(type);
        return total != null ? total : BigDecimal.ZERO;
    }

    private RecurringExpenseJpaEntity toEntity(RecurringExpense recurringExpense) {
        return new RecurringExpenseJpaEntity(
            recurringExpense.getId(),
            recurringExpense.getDescription(),
            recurringExpense.getAmount(),
            recurringExpense.getType()
        );
    }

    private RecurringExpense toDomain(RecurringExpenseJpaEntity entity) {
        return new RecurringExpense(
            entity.getId(),
            entity.getDescription(),
            entity.getAmount(),
            entity.getType()
        );
    }
}
