package cl.prezdev.balancehub.application.ports.out;

import cl.prezdev.balancehub.domain.FixedExpense;

public interface FixedExpenseRepository {
    void save(FixedExpense fixedExpense);
}
