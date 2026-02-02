package cl.prezdev.balancehub.application.usecases.recurringexpense.update;

import java.math.BigDecimal;

public record UpdateFixedExpenseResult (
    String id,
    String description,
    BigDecimal amount
) {}
