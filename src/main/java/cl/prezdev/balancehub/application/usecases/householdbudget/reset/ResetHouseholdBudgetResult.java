package cl.prezdev.balancehub.application.usecases.householdbudget.reset;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

public record ResetHouseholdBudgetResult(
    HouseholdBudgetCategory category,
    BigDecimal monthlyAmount,
    BigDecimal remainingAmount,
    Instant updatedAt
) {}
