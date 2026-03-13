package cl.prezdev.balancehub.application.usecases.pending.delete;

import cl.prezdev.balancehub.application.exception.PendingNotFoundException;
import cl.prezdev.balancehub.application.ports.in.DeletePendingInputPort;
import cl.prezdev.balancehub.application.ports.out.PendingRepository;

public class DeletePendingUseCase implements DeletePendingInputPort {

    private final PendingRepository repository;

    public DeletePendingUseCase(PendingRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("pendingRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public void execute(DeletePendingCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        repository.findById(command.id())
            .orElseThrow(() -> new PendingNotFoundException("Pending with id " + command.id() + " not found"));

        repository.deleteById(command.id());
    }
}
