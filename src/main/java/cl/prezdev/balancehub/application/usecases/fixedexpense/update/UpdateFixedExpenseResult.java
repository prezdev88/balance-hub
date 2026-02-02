package cl.prezdev.balancehub.application.usecases.fixedexpense.update;

public record UpdateFixedExpenseResult (
    String id,
    String description,
    double amount
) {}
