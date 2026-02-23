package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorCommand;
import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorResult;

public interface CreateDebtorInputPort {
    CreateDebtorResult execute(CreateDebtorCommand command);
}
