package cl.prezdev.balancehub.application.usecases.recurringexpense.list;

import java.util.List;

public record ListRecurringExpensesResult(
    List<RecurringExpenseListItem> recurringExpenses
) {}
