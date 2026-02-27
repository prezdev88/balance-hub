package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.debt.delete.DeleteDebtCommand;

public interface DeleteDebtInputPort {
    void execute(DeleteDebtCommand command);
}
