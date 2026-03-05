package cl.prezdev.balancehub.application.usecases.householdbudget.configure;

import java.math.BigDecimal;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

public record ConfigureHouseholdBudgetCommand(
    HouseholdBudgetCategory category,
    BigDecimal monthlyAmount
) {}
