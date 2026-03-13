package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.pending.delete.DeletePendingCommand;

public interface DeletePendingInputPort {
    void execute(DeletePendingCommand command);
}
