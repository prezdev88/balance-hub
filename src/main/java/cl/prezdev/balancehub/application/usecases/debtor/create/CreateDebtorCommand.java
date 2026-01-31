package cl.prezdev.balancehub.application.usecases.debtor.create;

public record CreateDebtorCommand(
    String name, String email
) {}
