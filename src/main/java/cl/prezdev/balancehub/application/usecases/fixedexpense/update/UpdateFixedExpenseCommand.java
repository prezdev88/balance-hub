package cl.prezdev.balancehub.application.usecases.fixedexpense.update;

public record UpdateFixedExpenseCommand (
    String id,
    String description,
    double amount
) {}
