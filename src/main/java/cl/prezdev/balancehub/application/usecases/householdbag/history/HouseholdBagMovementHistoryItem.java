package cl.prezdev.balancehub.application.usecases.householdbag.history;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.HouseholdBagMovementType;

public record HouseholdBagMovementHistoryItem(
    String id,
    BigDecimal amount,
    HouseholdBagMovementType type,
    Instant createdAt
) {}
