package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseResult;

public interface UpdateFixedExpenseInputPort {
    UpdateFixedExpenseResult execute(UpdateFixedExpenseCommand command);
}
