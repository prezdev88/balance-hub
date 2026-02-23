package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.installment.pay.PayInstallmentCommand;

public interface PayInstallmentInputPort {
    void execute(PayInstallmentCommand command);
}
