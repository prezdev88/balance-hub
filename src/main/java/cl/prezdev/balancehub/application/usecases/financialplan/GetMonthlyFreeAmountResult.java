package cl.prezdev.balancehub.application.usecases.financialplan;

import java.math.BigDecimal;
import java.util.List;

public record GetMonthlyFreeAmountResult(
    int year,
    BigDecimal currentSalary,
    BigDecimal monthlySavingsGoal,
    BigDecimal monthlyFixedExpenses,
    BigDecimal monthlyOptionalExpenses,
    BigDecimal monthlyFreeAmount,
    List<MonthlyFreeAmountItem> months
) {}
