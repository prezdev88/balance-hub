package cl.prezdev.balancehub.application.usecases.householdbag.movement;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.HouseholdBagMovementType;

public record RegisterHouseholdBagMovementResult(
    String bagId,
    BigDecimal amount,
    HouseholdBagMovementType movementType,
    BigDecimal remainingAmount,
    Instant updatedAt
) {}
