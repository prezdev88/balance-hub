package cl.prezdev.balancehub.application.usecases.householdbag;

import java.math.BigDecimal;
import java.time.Instant;

public record HouseholdBagDetails(
    String id,
    String name,
    String emoji,
    BigDecimal monthlyAmount,
    BigDecimal spentAmount,
    BigDecimal remainingAmount,
    Instant updatedAt
) {}
