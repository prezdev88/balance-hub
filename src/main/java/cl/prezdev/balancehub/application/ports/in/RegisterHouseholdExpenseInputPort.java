package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbudget.expense.RegisterHouseholdExpenseCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.expense.RegisterHouseholdExpenseResult;

public interface RegisterHouseholdExpenseInputPort {
    RegisterHouseholdExpenseResult execute(RegisterHouseholdExpenseCommand command);
}
