package cl.prezdev.balancehub.application.usecases.debt.get;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record InstallmentItem(
    String id,
    int number,
    LocalDate dueDate,
    Instant paidAt,
    BigDecimal amount
) {}
