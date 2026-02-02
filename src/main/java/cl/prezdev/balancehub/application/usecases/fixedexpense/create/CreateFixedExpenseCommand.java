package cl.prezdev.balancehub.application.usecases.fixedexpense.create;

import java.math.BigDecimal;

public record CreateFixedExpenseCommand(
    String description, BigDecimal amount
) {}
