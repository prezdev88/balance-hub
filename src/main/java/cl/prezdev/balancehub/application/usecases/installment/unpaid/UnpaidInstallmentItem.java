package cl.prezdev.balancehub.application.usecases.installment.unpaid;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record UnpaidInstallmentItem(
    String installmentId,
    String debtId,
    String debtDescription,
    int installmentNumber,
    int totalInstallments,
    LocalDate dueDate,
    BigDecimal amount,
    BigDecimal totalDebtAmount,
    boolean paid,
    Instant paidAt
) {}
