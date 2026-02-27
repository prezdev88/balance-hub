package cl.prezdev.balancehub.application.usecases.savingsgoal.create;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateSavingsGoalResult(
    String id,
    BigDecimal amount,
    Instant createdAt
) {}
