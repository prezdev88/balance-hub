package cl.prezdev.balancehub.application.usecases.recurringexpense.delete;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

class DeleteFixedExpenseUseCaseTest {

    private DeleteFixedExpenseUseCase useCase;
    private InMemoryRecurringExpenseRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryRecurringExpenseRepository();
        useCase = new DeleteFixedExpenseUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteFixedExpenseUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldThrowWhenNotFound() {
        assertThrows(FixedExpenseNotFoundException.class, () -> useCase.execute(new DeleteFixedExpenseCommand("no-id")));
    }

    @Test
    void shouldDeleteWhenFound() {
        repo.saved.add(new RecurringExpense("r-1", "Gym", BigDecimal.valueOf(100), ExpenseType.OPTIONAL));

        useCase.execute(new DeleteFixedExpenseCommand("r-1"));

        assertFalse(repo.findById("r-1").isPresent());
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
        public Optional<RecurringExpense> findById(String id) {
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
