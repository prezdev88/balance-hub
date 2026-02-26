package cl.prezdev.balancehub.application.usecases.recurringexpense.total;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.domain.RecurringExpense;
import cl.prezdev.balancehub.domain.enums.ExpenseType;

class GetRecurringExpenseTotalUseCaseTest {

    private GetRecurringExpenseTotalUseCase useCase;
    private InMemoryRecurringExpenseRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryRecurringExpenseRepository();
        useCase = new GetRecurringExpenseTotalUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new GetRecurringExpenseTotalUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldReturnZeroWhenNoExpenses() {
        var cmd = new GetRecurringExpenseTotalCommand(ExpenseType.FIXED);
        var result = useCase.execute(cmd);

        assertNotNull(result);
        assertEquals(0, result.total().compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldReturnTotalForFixedExpenses() {
        repo.saved.add(new RecurringExpense("1", "Rent", BigDecimal.valueOf(500), ExpenseType.FIXED));
        repo.saved.add(new RecurringExpense("2", "Internet", BigDecimal.valueOf(50), ExpenseType.FIXED));
        repo.saved.add(new RecurringExpense("3", "Entertainment", BigDecimal.valueOf(100), ExpenseType.OPTIONAL));

        var cmd = new GetRecurringExpenseTotalCommand(ExpenseType.FIXED);
        var result = useCase.execute(cmd);

        assertNotNull(result);
        assertEquals(0, result.total().compareTo(BigDecimal.valueOf(550)));
    }

    @Test
    void shouldReturnTotalForOptionalExpenses() {
        repo.saved.add(new RecurringExpense("1", "Rent", BigDecimal.valueOf(500), ExpenseType.FIXED));
        repo.saved.add(new RecurringExpense("2", "Gym", BigDecimal.valueOf(30), ExpenseType.OPTIONAL));
        repo.saved.add(new RecurringExpense("3", "Netflix", BigDecimal.valueOf(15), ExpenseType.OPTIONAL));

        var cmd = new GetRecurringExpenseTotalCommand(ExpenseType.OPTIONAL);
        var result = useCase.execute(cmd);

        assertNotNull(result);
        assertEquals(0, result.total().compareTo(BigDecimal.valueOf(45)));
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
