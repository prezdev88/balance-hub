package cl.prezdev.balancehub.application.usecases.householdbudget.expense;

import java.math.BigDecimal;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

public record RegisterHouseholdExpenseCommand(
    HouseholdBudgetCategory category,
    BigDecimal amount
) {}
