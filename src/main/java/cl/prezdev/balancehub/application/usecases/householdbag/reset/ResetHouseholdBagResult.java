package cl.prezdev.balancehub.application.usecases.householdbag.reset;

import java.math.BigDecimal;
import java.time.Instant;

public record ResetHouseholdBagResult(
    String bagId,
    BigDecimal monthlyAmount,
    BigDecimal remainingAmount,
    Instant updatedAt
) {}
