package cl.prezdev.balancehub.application.usecases.debt.update.command;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateDebtCommand(
    String debtId,
    String description,
    BigDecimal totalAmount,
    BigDecimal installmentAmount,
    LocalDate createdAt
) {}