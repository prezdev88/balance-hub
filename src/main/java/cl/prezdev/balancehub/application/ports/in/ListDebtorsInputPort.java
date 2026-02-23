package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.debtor.list.ListDebtorsResult;

public interface ListDebtorsInputPort {
    ListDebtorsResult execute();
}
