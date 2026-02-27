package cl.prezdev.balancehub.application.usecases.report.monthly;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record MonthlySummaryInstallmentItem(
    String installmentId,
    String debtDescription,
    int installmentNumber,
    int totalInstallments,
    LocalDate dueDate,
    BigDecimal amount,
    boolean paid,
    Instant paidAt
) {}
