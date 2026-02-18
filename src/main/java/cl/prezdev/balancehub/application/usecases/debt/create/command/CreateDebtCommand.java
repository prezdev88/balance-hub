package cl.prezdev.balancehub.application.usecases.debt.create.command;

public record CreateDebtCommand(
    DebtCommand debt,
    InstallmentCommand installments
) {}
