package cl.prezdev.balancehub.application.usecases.salarysnapshot.pay;

import java.time.Instant;

public record PayMonthlySalaryCommand(
    String debtorId,
    int year,
    int month,
    Instant paymentDate
) {}
