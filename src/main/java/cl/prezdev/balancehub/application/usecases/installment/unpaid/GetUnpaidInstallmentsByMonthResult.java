package cl.prezdev.balancehub.application.usecases.installment.unpaid;

import java.math.BigDecimal;
import java.util.List;

public record GetUnpaidInstallmentsByMonthResult(
    String debtorId,
    String debtorName,
    String debtorEmail,
    int year,
    int month,
    BigDecimal totalAmount,
    List<UnpaidInstallmentItem> installments
) {}
