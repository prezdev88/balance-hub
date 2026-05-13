package cl.prezdev.balancehub.application.usecases.householdbag.updatebudget;

import java.math.BigDecimal;

public record UpdateHouseholdBagBudgetCommand(
    String bagId,
    BigDecimal monthlyAmount
) {}
