package cl.prezdev.balancehub.application.usecases.fixedexpense.update;

public record UpdateFixedExpenseResponse (
    String id,
    String description,
    double amount
) {}
