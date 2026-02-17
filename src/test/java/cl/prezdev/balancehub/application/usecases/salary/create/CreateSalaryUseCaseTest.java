package cl.prezdev.balancehub.application.usecases.salary.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.SalaryRepository;
import cl.prezdev.balancehub.domain.Salary;
import cl.prezdev.balancehub.domain.exception.InvalidSalaryException;

class CreateSalaryUseCaseTest {

    private CreateSalaryUseCase useCase;
    private InMemorySalaryRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemorySalaryRepository();
        useCase = new CreateSalaryUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CreateSalaryUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldDeactivateAndSaveSalaryAndReturnResult() {
        var cmd = new CreateSalaryCommand(BigDecimal.valueOf(2000));

        var result = useCase.execute(cmd);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(1, repo.saved.size());
        assertEquals(0, repo.saved.get(0).getAmount().compareTo(BigDecimal.valueOf(2000)));
        assertEquals(1, repo.deactivateCount);
        assertNotNull(result.createdAt());
    }

    @Test
    void shouldThrowWhenAmountIsZero() {
        var cmd = new CreateSalaryCommand(BigDecimal.ZERO);
        assertThrows(InvalidSalaryException.class, () -> useCase.execute(cmd));
    }

    @Test
    void shouldThrowWhenAmountIsNegative() {
        var cmd = new CreateSalaryCommand(BigDecimal.valueOf(-1));
        assertThrows(InvalidSalaryException.class, () -> useCase.execute(cmd));
    }

    static class InMemorySalaryRepository implements SalaryRepository {

        List<Salary> saved = new ArrayList<>();
        int deactivateCount = 0;

        @Override
        public void save(Salary salary) {
            saved.add(salary);
        }

        @Override
        public void deactivateCurrentSalary() {
            deactivateCount++;
        }
    }
}
