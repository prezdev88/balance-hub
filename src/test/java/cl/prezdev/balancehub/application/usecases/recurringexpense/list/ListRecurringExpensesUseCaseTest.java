package cl.prezdev.balancehub.application.usecases.recurringexpense.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.domain.RecurringExpense;
import cl.prezdev.balancehub.domain.enums.ExpenseType;

class ListRecurringExpensesUseCaseTest {

    private ListRecurringExpensesUseCase useCase;
    private InMemoryRecurringExpenseRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryRecurringExpenseRepository();
        useCase = new ListRecurringExpensesUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListRecurringExpensesUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldListOnlyRequestedType() {
        repo.saved.add(new RecurringExpense("1", "A", BigDecimal.valueOf(10), ExpenseType.FIXED));
        repo.saved.add(new RecurringExpense("2", "B", BigDecimal.valueOf(20), ExpenseType.OPTIONAL));
        repo.saved.add(new RecurringExpense("3", "C", BigDecimal.valueOf(30), ExpenseType.FIXED));

        var cmd = new ListRecurringExpensesCommand(ExpenseType.FIXED);
        var result = useCase.execute(cmd);

        assertEquals(2, result.recurringExpenses().size());
        assertEquals("A", result.recurringExpenses().get(0).description());
        assertEquals(0, result.recurringExpenses().get(1).amount().compareTo(BigDecimal.valueOf(30)));
    }

    @Test
    void shouldReturnEmptyWhenNoMatching() {
        var cmd = new ListRecurringExpensesCommand(ExpenseType.OPTIONAL);
        var result = useCase.execute(cmd);

        assertEquals(0, result.recurringExpenses().size());
    }

    static class InMemoryRecurringExpenseRepository implements RecurringExpenseRepository {

        List<RecurringExpense> saved = new ArrayList<>();

        @Override
        public void save(RecurringExpense recurringExpense) {
            saved.add(recurringExpense);
        }

        @Override
        public void update(RecurringExpense recurringExpense) {
        }

        @Override
        public void deleteById(String id) {
            saved.removeIf(item -> item.getId().equals(id));
        }

        @Override
        public java.util.Optional<RecurringExpense> findById(String id) {
            return saved.stream().filter(r -> r.getId().equals(id)).findFirst();
        }

        @Override
        public List<RecurringExpense> findByType(ExpenseType type) {
            return saved.stream().filter(r -> r.getType() == type).toList();
        }

        @Override
        public BigDecimal totalByType(ExpenseType type) {
            return saved.stream()
                .filter(r -> r.getType() == type)
                .map(RecurringExpense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
