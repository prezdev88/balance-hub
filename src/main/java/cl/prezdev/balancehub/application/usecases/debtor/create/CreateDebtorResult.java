package cl.prezdev.balancehub.application.usecases.debtor.create;

import cl.prezdev.balancehub.domain.DebtorId;

public record CreateDebtorResult(
    DebtorId debtorId
) {}
