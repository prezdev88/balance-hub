package cl.prezdev.balancehub.application.usecases.debtor.list;

import java.util.List;

public record ListDebtorsResult (
    List<DebtorListItem> debtors
) {}