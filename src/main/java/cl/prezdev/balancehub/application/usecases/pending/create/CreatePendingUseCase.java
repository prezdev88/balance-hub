package cl.prezdev.balancehub.application.usecases.pending.create;

import cl.prezdev.balancehub.application.ports.in.CreatePendingInputPort;
import cl.prezdev.balancehub.application.ports.out.PendingRepository;
import cl.prezdev.balancehub.domain.Pending;

public class CreatePendingUseCase implements CreatePendingInputPort {

    private final PendingRepository repository;

    public CreatePendingUseCase(PendingRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("pendingRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public CreatePendingResult execute(CreatePendingCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        Pending pending = new Pending(command.description());
        repository.save(pending);
        return new CreatePendingResult(pending.getId(), pending.getDescription(), pending.getCreatedAt());
    }
}
