package cl.prezdev.balancehub.application.usecases.installment.pay;

import java.time.Instant;

public record PayInstallmentCommand(
    String installmentId,
    Instant paymentDate
) {}
