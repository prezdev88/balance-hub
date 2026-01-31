package cl.prezdev.balancehub.application.usecases.debtor.create;

import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.domain.Debtor;

public class CreateDebtorUseCase {

    private final DebtorRepository repository;

    public CreateDebtorUseCase(DebtorRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");  
        }

        this.repository = repository;
    }

    public CreateDebtorResult execute(CreateDebtorCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");   
        }

        Debtor debtor = new Debtor(command.name(), command.email());
        repository.save(debtor);
        return new CreateDebtorResult(debtor.getId());
    }
}
