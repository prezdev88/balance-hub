package cl.prezdev.balancehub.application.usecases.installment.unpaid;

public record GetUnpaidInstallmentsByMonthCommand(
    String debtorId,
    int year,
    int month
) {}
