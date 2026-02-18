package cl.prezdev.balancehub.application.usecases.debt.create.command;

import java.math.BigDecimal;

public record DebtCommand(
    String debtorId,
    String description, 
    BigDecimal totalAmount
) {}
