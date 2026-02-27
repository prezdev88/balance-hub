package cl.prezdev.balancehub.application.usecases.savingsgoal.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.SavingsGoalRepository;
import cl.prezdev.balancehub.domain.SavingsGoal;
import cl.prezdev.balancehub.domain.exception.InvalidSavingsGoalException;

class CreateSavingsGoalUseCaseTest {

    private CreateSavingsGoalUseCase useCase;
    private InMemorySavingsGoalRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemorySavingsGoalRepository();
        useCase = new CreateSavingsGoalUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CreateSavingsGoalUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldDeactivateAndSaveSavingsGoalAndReturnResult() {
        var result = useCase.execute(new CreateSavingsGoalCommand(BigDecimal.valueOf(350000)));

        assertNotNull(result.id());
        assertNotNull(result.createdAt());
        assertEquals(1, repo.saved.size());
        assertEquals(1, repo.deactivateCount);
        assertEquals(0, repo.saved.get(0).getAmount().compareTo(BigDecimal.valueOf(350000)));
    }

    @Test
    void shouldThrowWhenAmountIsZero() {
        assertThrows(InvalidSavingsGoalException.class, () -> useCase.execute(new CreateSavingsGoalCommand(BigDecimal.ZERO)));
    }

    static class InMemorySavingsGoalRepository implements SavingsGoalRepository {

        final List<SavingsGoal> saved = new ArrayList<>();
        int deactivateCount = 0;

        @Override
        public void save(SavingsGoal savingsGoal) {
            saved.add(savingsGoal);
        }

        @Override
        public void deactivateCurrentSavingsGoal() {
            deactivateCount++;
        }

        @Override
        public Optional<SavingsGoal> findActive() {
            return saved.stream().filter(SavingsGoal::isActive).findFirst();
        }
    }
}
