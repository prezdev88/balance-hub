package cl.prezdev.balancehub.application.usecases.debt.get;

import java.util.List;

public record GetDebtsResult(
    String id,
    String name,
    String email,
    List<DebtItem> debts
) {}
