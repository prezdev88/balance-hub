package cl.prezdev.balancehub.application.usecases.recurringexpense.list;

import cl.prezdev.balancehub.domain.enums.ExpenseType;

public record ListRecurringExpensesCommand(
    ExpenseType type
) {}
