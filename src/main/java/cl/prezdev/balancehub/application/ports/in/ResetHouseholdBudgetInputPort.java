package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbudget.reset.ResetHouseholdBudgetCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.reset.ResetHouseholdBudgetResult;

public interface ResetHouseholdBudgetInputPort {
    ResetHouseholdBudgetResult execute(ResetHouseholdBudgetCommand command);
}
