package cl.prezdev.balancehub.application.usecases.recurringexpense.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.domain.RecurringExpense;
import cl.prezdev.balancehub.domain.enums.ExpenseType;
import cl.prezdev.balancehub.domain.exception.InvalidRecurringExpenseException;

class CreateRecurringExpenseUseCaseTest {

    private CreateRecurringExpenseUseCase useCase;
    private InMemoryRecurringExpenseRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryRecurringExpenseRepository();
        useCase = new CreateRecurringExpenseUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CreateRecurringExpenseUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldSaveRecurringExpenseAndReturnId() {
        var cmd = new CreateRecurringExpenseCommand("Rent", BigDecimal.valueOf(500), ExpenseType.FIXED);

        var result = useCase.execute(cmd);

        assertNotNull(result);
        assertNotNull(result.recurringExpenseId());
        assertEquals(1, repo.saved.size());
        assertEquals("Rent", repo.lastSaved.getDescription());
        assertEquals(0, repo.lastSaved.getAmount().compareTo(BigDecimal.valueOf(500)));
    }

    @Test
    void shouldThrowWhenDescriptionIsBlank() {
        var cmd = new CreateRecurringExpenseCommand("", BigDecimal.valueOf(10), ExpenseType.FIXED);
        assertThrows(InvalidRecurringExpenseException.class, () -> useCase.execute(cmd));
    }

    @Test
    void shouldThrowWhenAmountIsZero() {
        var cmd = new CreateRecurringExpenseCommand("Desc", BigDecimal.valueOf(0), ExpenseType.FIXED);
        assertThrows(InvalidRecurringExpenseException.class, () -> useCase.execute(cmd));
    }

    @Test
    void shouldThrowWhenTypeIsNull() {
        var cmd = new CreateRecurringExpenseCommand("Desc", BigDecimal.valueOf(10), null);
        assertThrows(InvalidRecurringExpenseException.class, () -> useCase.execute(cmd));
    }

    static class InMemoryRecurringExpenseRepository implements RecurringExpenseRepository {

        RecurringExpense lastSaved;
        List<RecurringExpense> saved = new ArrayList<>();

        @Override
        public void save(RecurringExpense recurringExpense) {
            lastSaved = recurringExpense;
            saved.add(recurringExpense);
        }

        @Override
        public void update(RecurringExpense recurringExpense) {
            // not needed for this test
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
