package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbudget.configure.ConfigureHouseholdBudgetCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.configure.ConfigureHouseholdBudgetResult;

public interface ConfigureHouseholdBudgetInputPort {
    ConfigureHouseholdBudgetResult execute(ConfigureHouseholdBudgetCommand command);
}
