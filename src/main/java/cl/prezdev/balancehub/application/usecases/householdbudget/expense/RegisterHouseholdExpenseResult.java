package cl.prezdev.balancehub.application.usecases.householdbudget.expense;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

public record RegisterHouseholdExpenseResult(
    HouseholdBudgetCategory category,
    BigDecimal consumedAmount,
    BigDecimal remainingAmount,
    Instant updatedAt
) {}
