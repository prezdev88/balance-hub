package cl.prezdev.balancehub.application.usecases.recurringexpense.create;

import java.math.BigDecimal;

import cl.prezdev.balancehub.domain.enums.ExpenseType;

public record CreateRecurringExpenseCommand(
    String description, BigDecimal amount, ExpenseType type
) {}
