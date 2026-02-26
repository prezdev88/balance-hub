package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.recurringexpense.delete.DeleteFixedExpenseCommand;

public interface DeleteFixedExpenseInputPort {
    void execute(DeleteFixedExpenseCommand command);
}
