package cl.prezdev.balancehub.application.usecases.recurringexpense.total;

import java.math.BigDecimal;

public record GetRecurringExpenseTotalResult (
    BigDecimal total
) {}
