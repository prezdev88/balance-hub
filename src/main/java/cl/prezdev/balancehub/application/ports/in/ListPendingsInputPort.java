package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.pending.list.ListPendingsCommand;
import cl.prezdev.balancehub.application.usecases.pending.list.ListPendingsResult;

public interface ListPendingsInputPort {
    ListPendingsResult execute(ListPendingsCommand command);
}
