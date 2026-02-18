package cl.prezdev.balancehub.application.usecases.debt.create.command;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentCommand(
    int installmentsCount,
    BigDecimal installmentAmount,
    LocalDate firstInstallmentDueDate // fecha de vencimiento de la primera cuota
) {}
