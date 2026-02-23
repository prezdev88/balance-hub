package cl.prezdev.balancehub.application.usecases.recurringexpense.list;

import cl.prezdev.balancehub.application.ports.in.ListRecurringExpensesInputPort;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;

public class ListRecurringExpensesUseCase implements ListRecurringExpensesInputPort {

    private final RecurringExpenseRepository repository;

    public ListRecurringExpensesUseCase(RecurringExpenseRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }

        this.repository = repository;
    }

    @Override
    public ListRecurringExpensesResult execute(ListRecurringExpensesCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var recurringExpenses = repository.findByType(command.type())
            .stream().map(recurringExpense -> new RecurringExpenseListItem(
                recurringExpense.getId(),
                recurringExpense.getDescription(),
                recurringExpense.getAmount()
            )).toList();

        return new ListRecurringExpensesResult(recurringExpenses);
    }
}
