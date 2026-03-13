package cl.prezdev.balancehub.application.usecases.pending.list;

import cl.prezdev.balancehub.application.ports.in.ListPendingsInputPort;
import cl.prezdev.balancehub.application.ports.out.PendingRepository;

public class ListPendingsUseCase implements ListPendingsInputPort {

    private final PendingRepository repository;

    public ListPendingsUseCase(PendingRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("pendingRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public ListPendingsResult execute(ListPendingsCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        return new ListPendingsResult(
            repository.findAll().stream()
                .map(pending -> new PendingListItem(
                    pending.getId(),
                    pending.getDescription(),
                    pending.getCreatedAt()
                ))
                .toList()
        );
    }
}
