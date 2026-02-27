package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.installment.unpaid.GetUnpaidInstallmentsByMonthCommand;
import cl.prezdev.balancehub.application.usecases.installment.unpaid.GetUnpaidInstallmentsByMonthResult;

public interface GetUnpaidInstallmentsByMonthInputPort {
    GetUnpaidInstallmentsByMonthResult execute(GetUnpaidInstallmentsByMonthCommand command);
}
