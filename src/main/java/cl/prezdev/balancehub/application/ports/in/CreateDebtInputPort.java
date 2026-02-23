package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.debt.create.CreateDebtResult;
import cl.prezdev.balancehub.application.usecases.debt.create.command.CreateDebtCommand;

public interface CreateDebtInputPort {
    CreateDebtResult execute(CreateDebtCommand command);
}
