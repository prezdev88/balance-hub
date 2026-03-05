package cl.prezdev.balancehub.application.usecases.householdbudget.summary;

import java.math.BigDecimal;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

public record HouseholdBudgetSummaryItem(
    HouseholdBudgetCategory category,
    BigDecimal monthlyAmount,
    BigDecimal spentAmount,
    BigDecimal remainingAmount
) {}
