package cl.prezdev.balancehub.application.usecases.fixedexpense.create;

public record CreateFixedExpenseCommand(
    String description, double amount
) {}
