package cl.prezdev.balancehub.application.usecases.fixedexpense.update;

import cl.prezdev.balancehub.application.ports.out.FixedExpenseRepository;
import cl.prezdev.balancehub.domain.FixedExpense;

public class UpdateFixedExpenseUseCase {

    private final FixedExpenseRepository repository;

    public UpdateFixedExpenseUseCase(FixedExpenseRepository repository) {
        if (repository == null){
            throw new IllegalArgumentException("repository cannot be null");
        }

        this.repository = repository;
    }

    public UpdateFixedExpenseResponse execute(UpdateFixedExpenseCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        var fixedExpense = new FixedExpense (
            command.id(),
            command.description(),
            command.amount()
        );

        repository.save(fixedExpense);

        return new UpdateFixedExpenseResponse (
            fixedExpense.getId(),
            fixedExpense.getDescription(),
            fixedExpense.getAmount()
        );
    }
}
