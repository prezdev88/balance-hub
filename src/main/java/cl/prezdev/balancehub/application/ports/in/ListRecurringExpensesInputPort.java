package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesResult;

public interface ListRecurringExpensesInputPort {
    ListRecurringExpensesResult execute(ListRecurringExpensesCommand command);
}
