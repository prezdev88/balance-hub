package cl.prezdev.balancehub.application.usecases.recurringexpense.list;

import java.math.BigDecimal;

public record RecurringExpenseListItem(
    String id,
    String description,
    BigDecimal amount
) {}
