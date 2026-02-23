package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalResult;

public interface GetRecurringExpenseTotalInputPort {
    GetRecurringExpenseTotalResult execute(GetRecurringExpenseTotalCommand command);
}
