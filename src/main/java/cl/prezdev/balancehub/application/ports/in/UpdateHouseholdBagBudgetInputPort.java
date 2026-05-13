package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbag.updatebudget.UpdateHouseholdBagBudgetCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.updatebudget.UpdateHouseholdBagBudgetResult;

public interface UpdateHouseholdBagBudgetInputPort {
    UpdateHouseholdBagBudgetResult execute(UpdateHouseholdBagBudgetCommand command);
}
