package cl.prezdev.balancehub.application.usecases.recurringexpense.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.exception.FixedExpenseNotFoundException;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.domain.RecurringExpense;
import cl.prezdev.balancehub.domain.enums.ExpenseType;
import cl.prezdev.balancehub.domain.exception.InvalidRecurringExpenseException;

class UpdateFixedExpenseUseCaseTest {

    private UpdateFixedExpenseUseCase useCase;
    private InMemoryRecurringExpenseRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryRecurringExpenseRepository();
        useCase = new UpdateFixedExpenseUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new UpdateFixedExpenseUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldThrowWhenNotFound() {
        var cmd = new UpdateFixedExpenseCommand("no-id", "X", BigDecimal.ONE);
        assertThrows(FixedExpenseNotFoundException.class, () -> useCase.execute(cmd));
    }

    @Test
    void shouldUpdateAndReturnUpdatedValues() {
        var existing = new RecurringExpense("r-1", "Old", BigDecimal.valueOf(100), ExpenseType.FIXED);
        repo.saved.add(existing);

        var cmd = new UpdateFixedExpenseCommand(existing.getId(), "New Desc", BigDecimal.valueOf(150));
        var result = useCase.execute(cmd);

        assertEquals(existing.getId(), result.id());
        assertEquals("New Desc", result.description());
        assertEquals(0, result.amount().compareTo(BigDecimal.valueOf(150)));
        assertEquals("New Desc", repo.lastUpdated.getDescription());
    }

    @Test
    void shouldThrowWhenNewDescriptionIsBlank() {
        var existing = new RecurringExpense("r-2", "Old2", BigDecimal.valueOf(100), ExpenseType.FIXED);
        repo.saved.add(existing);

        var cmd = new UpdateFixedExpenseCommand(existing.getId(), "", BigDecimal.valueOf(50));
        assertThrows(InvalidRecurringExpenseException.class, () -> useCase.execute(cmd));
    }

    @Test
    void shouldThrowWhenNewAmountIsInvalid() {
        var existing = new RecurringExpense("r-3", "Old3", BigDecimal.valueOf(100), ExpenseType.FIXED);
        repo.saved.add(existing);

        var cmd = new UpdateFixedExpenseCommand(existing.getId(), "New", BigDecimal.valueOf(0));
        assertThrows(InvalidRecurringExpenseException.class, () -> useCase.execute(cmd));
    }

    static class InMemoryRecurringExpenseRepository implements RecurringExpenseRepository {

        List<RecurringExpense> saved = new ArrayList<>();
        RecurringExpense lastUpdated;

        @Override
        public void save(RecurringExpense recurringExpense) {
            saved.add(recurringExpense);
        }

        @Override
        public void update(RecurringExpense recurringExpense) {
            this.lastUpdated = recurringExpense;
        }

        @Override
        public Optional<RecurringExpense> findById(String id) {
            return saved.stream().filter(r -> r.getId().equals(id)).findFirst();
        }

        @Override
        public List<RecurringExpense> findByType(ExpenseType type) {
            return saved.stream().filter(r -> r.getType() == type).toList();
        }
    }
}
