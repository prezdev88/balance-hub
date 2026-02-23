package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.debt.get.GetDebtsCommand;
import cl.prezdev.balancehub.application.usecases.debt.get.GetDebtsResult;

public interface GetDebtsInputPort {
    GetDebtsResult execute(GetDebtsCommand command);
}
