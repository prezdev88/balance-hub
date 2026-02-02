package cl.prezdev.balancehub.application.usecases.recurringexpense.update;

import cl.prezdev.balancehub.application.exception.FixedExpenseNotFoundException;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.domain.RecurringExpense;

public class UpdateFixedExpenseUseCase {

    private final RecurringExpenseRepository repository;

    public UpdateFixedExpenseUseCase(RecurringExpenseRepository repository) {
        if (repository == null){
            throw new IllegalArgumentException("repository cannot be null");
        }

        this.repository = repository;
    }

    public UpdateFixedExpenseResult execute(UpdateFixedExpenseCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        RecurringExpense fixedExpense = repository.findById(command.id()).orElseThrow(
            () -> new FixedExpenseNotFoundException("FixedExpense with id " + command.id() + " not found")
        );

        fixedExpense.setDescription(command.description());
        fixedExpense.setAmount(command.amount());

        repository.update(fixedExpense);

        return new UpdateFixedExpenseResult (
            fixedExpense.getId(),
            fixedExpense.getDescription(),
            fixedExpense.getAmount()
        );
    }
}
