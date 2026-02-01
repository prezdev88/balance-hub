package cl.prezdev.balancehub.application.usecases.fixedexpense.create;

import cl.prezdev.balancehub.application.ports.out.FixedExpenseRepository;
import cl.prezdev.balancehub.domain.FixedExpense;

public class CreateFixedExpenseUseCase {

    private final FixedExpenseRepository repository;

    public CreateFixedExpenseUseCase(FixedExpenseRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }

        this.repository = repository;
    }

    public CreateFixedExpenseResult execute(CreateFixedExpenseCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var fixedExpense = new FixedExpense (
            command.description(),
            command.amount()
        );

        repository.save(fixedExpense);
        
        return new CreateFixedExpenseResult(fixedExpense.getId());
    }
}
