package cl.prezdev.balancehub.application.usecases.householdbudget.reset;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

public record ResetHouseholdBudgetCommand(HouseholdBudgetCategory category) {}
