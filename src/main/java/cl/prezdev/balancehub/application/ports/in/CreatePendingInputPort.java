package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.pending.create.CreatePendingCommand;
import cl.prezdev.balancehub.application.usecases.pending.create.CreatePendingResult;

public interface CreatePendingInputPort {
    CreatePendingResult execute(CreatePendingCommand command);
}
