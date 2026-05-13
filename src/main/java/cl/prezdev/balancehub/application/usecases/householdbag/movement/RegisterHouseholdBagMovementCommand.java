package cl.prezdev.balancehub.application.usecases.householdbag.movement;

import java.math.BigDecimal;

public record RegisterHouseholdBagMovementCommand(
    String bagId,
    BigDecimal amount
) {}
