package cl.prezdev.balancehub.application.usecases.salary.create;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateSalaryResult (
    String id,
    BigDecimal amount,
    Instant createdAt
) {}