package cl.prezdev.balancehub.application.ports.out;

import java.util.Optional;

import cl.prezdev.balancehub.domain.RecurringExpense;

public interface RecurringExpenseRepository {
    void save(RecurringExpense fixedExpense);

    void update(RecurringExpense fixedExpense);

    Optional<RecurringExpense> findById(String id);
}
