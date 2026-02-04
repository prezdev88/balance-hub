package cl.prezdev.balancehub.application.usecases.recurringexpense.total;

import cl.prezdev.balancehub.domain.enums.ExpenseType;

public record GetRecurringExpenseTotalCommand (
    ExpenseType type
) {}
