package cl.prezdev.balancehub.application.ports.out;

import java.util.List;
import java.util.Optional;

import cl.prezdev.balancehub.domain.RecurringExpense;
import cl.prezdev.balancehub.domain.enums.ExpenseType;

public interface RecurringExpenseRepository {
    void save(RecurringExpense recurringExpense);

    void update(RecurringExpense recurringExpense);

    Optional<RecurringExpense> findById(String id);

    List<RecurringExpense> findByType(ExpenseType type);
}
