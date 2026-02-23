package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.recurringexpense.create.CreateRecurringExpenseCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.create.CreateRecurringExpenseResult;

public interface CreateRecurringExpenseInputPort {
    CreateRecurringExpenseResult execute(CreateRecurringExpenseCommand command);
}
