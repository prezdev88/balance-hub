package cl.prezdev.balancehub.application.usecases.fixedexpense.update;

import java.math.BigDecimal;

public record UpdateFixedExpenseCommand (
    String id,
    String description,
    BigDecimal amount
) {}
