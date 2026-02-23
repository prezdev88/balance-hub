package cl.prezdev.balancehub.application.usecases.recurringexpense.create;

import cl.prezdev.balancehub.application.ports.in.CreateRecurringExpenseInputPort;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.domain.RecurringExpense;

public class CreateRecurringExpenseUseCase implements CreateRecurringExpenseInputPort {

    private final RecurringExpenseRepository repository;

    public CreateRecurringExpenseUseCase(RecurringExpenseRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }

        this.repository = repository;
    }

    @Override
    public CreateRecurringExpenseResult execute(CreateRecurringExpenseCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var recurringExpense = new RecurringExpense (
            command.description(),
            command.amount(),
            command.type()
        );

        repository.save(recurringExpense);
        
        return new CreateRecurringExpenseResult(recurringExpense.getId());
    }
}
