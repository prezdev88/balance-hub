package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbudget.summary.GetHouseholdBudgetSummaryCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.summary.GetHouseholdBudgetSummaryResult;

public interface GetHouseholdBudgetSummaryInputPort {
    GetHouseholdBudgetSummaryResult execute(GetHouseholdBudgetSummaryCommand command);
}
