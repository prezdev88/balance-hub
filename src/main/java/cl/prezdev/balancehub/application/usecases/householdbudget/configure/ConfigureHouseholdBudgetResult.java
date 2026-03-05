package cl.prezdev.balancehub.application.usecases.householdbudget.configure;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

public record ConfigureHouseholdBudgetResult(
    HouseholdBudgetCategory category,
    BigDecimal monthlyAmount,
    BigDecimal remainingAmount,
    Instant updatedAt
) {}
