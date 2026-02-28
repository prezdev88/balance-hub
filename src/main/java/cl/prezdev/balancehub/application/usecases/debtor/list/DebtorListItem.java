package cl.prezdev.balancehub.application.usecases.debtor.list;

import java.math.BigDecimal;

public record DebtorListItem (
    String id,
    String name,
    String email,
    BigDecimal totalDebt,
    boolean accessEnabled
) {}
