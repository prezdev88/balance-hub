package cl.prezdev.balancehub.application.usecases.householdbudget.summary;

import java.util.List;

public record GetHouseholdBudgetSummaryResult(
    List<HouseholdBudgetSummaryItem> budgets
) {}
