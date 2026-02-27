package cl.prezdev.balancehub.application.usecases.debt.getdetail;

import cl.prezdev.balancehub.application.usecases.debt.get.DebtItem;

public record GetDebtDetailResult(
    String id,
    String name,
    String email,
    DebtItem debt
) {}
