package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.debt.getdetail.GetDebtDetailCommand;
import cl.prezdev.balancehub.application.usecases.debt.getdetail.GetDebtDetailResult;

public interface GetDebtDetailInputPort {
    GetDebtDetailResult execute(GetDebtDetailCommand command);
}
