package cl.prezdev.balancehub.application.usecases.debt.get;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record DebtItem(
    String id,
    String description, 
    BigDecimal totalAmount,
    Instant createdAt,
    boolean settled,
    List<InstallmentItem> installments
) {}
