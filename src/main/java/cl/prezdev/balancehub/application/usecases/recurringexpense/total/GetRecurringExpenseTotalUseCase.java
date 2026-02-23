package cl.prezdev.balancehub.application.usecases.recurringexpense.total;

import cl.prezdev.balancehub.application.ports.in.GetRecurringExpenseTotalInputPort;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;

public class GetRecurringExpenseTotalUseCase implements GetRecurringExpenseTotalInputPort {

    private final RecurringExpenseRepository repository;

    public GetRecurringExpenseTotalUseCase(RecurringExpenseRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("RecurringExpenseRepository must not be null");
        }

        this.repository = repository;
    }

    @Override
    public GetRecurringExpenseTotalResult execute(GetRecurringExpenseTotalCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var total = repository.totalByType(command.type());
        return new GetRecurringExpenseTotalResult(total);
    }
}
