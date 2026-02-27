package cl.prezdev.balancehub.application.usecases.financialplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.SalaryRepository;
import cl.prezdev.balancehub.application.ports.out.SavingsGoalRepository;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.domain.Salary;
import cl.prezdev.balancehub.domain.RecurringExpense;
import cl.prezdev.balancehub.domain.SavingsGoal;
import cl.prezdev.balancehub.domain.enums.ExpenseType;

class GetMonthlyFreeAmountUseCaseTest {

    private InMemorySalaryRepository salaryRepository;
    private InMemorySavingsGoalRepository savingsGoalRepository;
    private InMemoryRecurringExpenseRepository recurringExpenseRepository;
    private GetMonthlyFreeAmountUseCase useCase;

    @BeforeEach
    void setUp() {
        salaryRepository = new InMemorySalaryRepository();
        savingsGoalRepository = new InMemorySavingsGoalRepository();
        recurringExpenseRepository = new InMemoryRecurringExpenseRepository();
        useCase = new GetMonthlyFreeAmountUseCase(salaryRepository, savingsGoalRepository, recurringExpenseRepository);
    }

    @Test
    void shouldCalculateFreeAmountForAllMonths() {
        salaryRepository.active = new Salary("salary-1", BigDecimal.valueOf(1200000), Instant.now(), true);
        savingsGoalRepository.active = new SavingsGoal("goal-1", BigDecimal.valueOf(300000), Instant.now(), true);
        recurringExpenseRepository.fixedTotal = BigDecimal.valueOf(200000);
        recurringExpenseRepository.optionalTotal = BigDecimal.valueOf(100000);

        var result = useCase.execute(new GetMonthlyFreeAmountCommand(2026));

        assertEquals(12, result.months().size());
        assertEquals(0, result.currentSalary().compareTo(BigDecimal.valueOf(1200000)));
        assertEquals(0, result.monthlySavingsGoal().compareTo(BigDecimal.valueOf(300000)));
        assertEquals(0, result.monthlyFixedExpenses().compareTo(BigDecimal.valueOf(200000)));
        assertEquals(0, result.monthlyOptionalExpenses().compareTo(BigDecimal.valueOf(100000)));
        assertEquals(0, result.monthlyFreeAmount().compareTo(BigDecimal.valueOf(600000)));
        assertEquals(0, result.months().get(0).freeAmount().compareTo(BigDecimal.valueOf(600000)));
    }

    @Test
    void shouldUseZeroWhenSalaryOrSavingsAreMissing() {
        var result = useCase.execute(new GetMonthlyFreeAmountCommand(2026));

        assertEquals(0, result.currentSalary().compareTo(BigDecimal.ZERO));
        assertEquals(0, result.monthlySavingsGoal().compareTo(BigDecimal.ZERO));
        assertEquals(0, result.monthlyFixedExpenses().compareTo(BigDecimal.ZERO));
        assertEquals(0, result.monthlyOptionalExpenses().compareTo(BigDecimal.ZERO));
        assertEquals(0, result.monthlyFreeAmount().compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldThrowOnInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(new GetMonthlyFreeAmountCommand(1200)));
    }

    static class InMemorySalaryRepository implements SalaryRepository {
        Salary active;

        @Override
        public void save(Salary salary) {}

        @Override
        public void deactivateCurrentSalary() {}

        @Override
        public Optional<Salary> findActive() {
            return Optional.ofNullable(active);
        }
    }

    static class InMemorySavingsGoalRepository implements SavingsGoalRepository {
        SavingsGoal active;

        @Override
        public void save(SavingsGoal savingsGoal) {}

        @Override
        public void deactivateCurrentSavingsGoal() {}

        @Override
        public Optional<SavingsGoal> findActive() {
            return Optional.ofNullable(active);
        }
    }

    static class InMemoryRecurringExpenseRepository implements RecurringExpenseRepository {
        BigDecimal fixedTotal = BigDecimal.ZERO;
        BigDecimal optionalTotal = BigDecimal.ZERO;

        @Override
        public void save(RecurringExpense recurringExpense) {}

        @Override
        public void update(RecurringExpense recurringExpense) {}

        @Override
        public void deleteById(String id) {}

        @Override
        public Optional<RecurringExpense> findById(String id) {
            return Optional.empty();
        }

        @Override
        public List<RecurringExpense> findByType(ExpenseType type) {
            return List.of();
        }

        @Override
        public BigDecimal totalByType(ExpenseType type) {
            return type == ExpenseType.FIXED ? fixedTotal : optionalTotal;
        }
    }
}
