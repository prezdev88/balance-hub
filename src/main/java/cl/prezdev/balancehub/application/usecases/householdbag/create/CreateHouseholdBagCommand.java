package cl.prezdev.balancehub.application.usecases.householdbag.create;

import java.math.BigDecimal;

public record CreateHouseholdBagCommand(
    String name,
    String emoji,
    BigDecimal monthlyAmount
) {}
