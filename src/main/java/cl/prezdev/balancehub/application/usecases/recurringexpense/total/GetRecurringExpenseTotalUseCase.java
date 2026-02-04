package cl.prezdev.balancehub.application.usecases.recurringexpense.total;

import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;

public class GetRecurringExpenseTotalUseCase {

    private final RecurringExpenseRepository repository;

    public GetRecurringExpenseTotalUseCase(RecurringExpenseRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("RecurringExpenseRepository must not be null");
        }

        this.repository = repository;
    }

    public GetRecurringExpenseTotalResult execute(GetRecurringExpenseTotalCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var total = repository.totalByType(command.type());
        return new GetRecurringExpenseTotalResult(total);
    }
}
