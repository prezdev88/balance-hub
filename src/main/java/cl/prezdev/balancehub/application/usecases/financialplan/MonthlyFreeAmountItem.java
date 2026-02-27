package cl.prezdev.balancehub.application.usecases.financialplan;

import java.math.BigDecimal;

public record MonthlyFreeAmountItem(
    int month,
    int year,
    BigDecimal freeAmount
) {}
