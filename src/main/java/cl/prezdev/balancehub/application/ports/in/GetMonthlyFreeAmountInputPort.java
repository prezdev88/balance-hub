package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.financialplan.GetMonthlyFreeAmountCommand;
import cl.prezdev.balancehub.application.usecases.financialplan.GetMonthlyFreeAmountResult;

public interface GetMonthlyFreeAmountInputPort {
    GetMonthlyFreeAmountResult execute(GetMonthlyFreeAmountCommand command);
}
