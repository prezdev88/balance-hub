package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.debt.update.UpdateDebtResult;
import cl.prezdev.balancehub.application.usecases.debt.update.command.UpdateDebtCommand;

public interface UpdateDebtInputPort {

    UpdateDebtResult execute(UpdateDebtCommand command);
}