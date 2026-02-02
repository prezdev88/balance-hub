package cl.prezdev.balancehub.application.ports.out;

import java.util.Optional;

import cl.prezdev.balancehub.domain.FixedExpense;

public interface FixedExpenseRepository {
    void save(FixedExpense fixedExpense);

    void update(FixedExpense fixedExpense);

    Optional<FixedExpense> findById(String id);
}
