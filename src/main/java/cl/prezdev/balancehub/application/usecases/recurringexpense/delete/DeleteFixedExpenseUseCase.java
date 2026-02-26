package cl.prezdev.balancehub.application.usecases.recurringexpense.delete;

import cl.prezdev.balancehub.application.exception.FixedExpenseNotFoundException;
import cl.prezdev.balancehub.application.ports.in.DeleteFixedExpenseInputPort;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;

public class DeleteFixedExpenseUseCase implements DeleteFixedExpenseInputPort {

    private final RecurringExpenseRepository repository;

    public DeleteFixedExpenseUseCase(RecurringExpenseRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public void execute(DeleteFixedExpenseCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        repository.findById(command.id()).orElseThrow(
            () -> new FixedExpenseNotFoundException("FixedExpense with id " + command.id() + " not found")
        );

        repository.deleteById(command.id());
    }
}
