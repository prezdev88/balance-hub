package cl.prezdev.balancehub.application.usecases.debt.delete;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.exception.DebtNotFoundException;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.domain.Debt;

class DeleteDebtUseCaseTest {

    private DeleteDebtUseCase useCase;
    private InMemoryDebtRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryDebtRepository();
        useCase = new DeleteDebtUseCase(repository);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new DeleteDebtUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldThrowWhenDebtDoesNotExist() {
        assertThrows(DebtNotFoundException.class, () -> useCase.execute(new DeleteDebtCommand("missing")));
    }

    @Test
    void shouldDeleteDebtWhenExists() {
        repository.saved.add(new Debt("d-1", "Laptop", BigDecimal.valueOf(1000), "debtor-1", Instant.now(), false));

        useCase.execute(new DeleteDebtCommand("d-1"));

        assertFalse(repository.findById("d-1").isPresent());
    }

    static class InMemoryDebtRepository implements DebtRepository {
        List<Debt> saved = new ArrayList<>();

        @Override
        public void save(Debt debt) {
            saved.add(debt);
        }

        @Override
        public Optional<Debt> findById(String debtId) {
            return saved.stream().filter(d -> d.getId().equals(debtId)).findFirst();
        }

        @Override
        public void deleteById(String debtId) {
            saved.removeIf(d -> d.getId().equals(debtId));
        }

        @Override
        public BigDecimal totalByDebtorId(String debtorId) {
            return saved.stream()
                .filter(d -> d.getDebtorId().equals(debtorId))
                .map(Debt::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        @Override
        public List<Debt> findByDebtorIdAndDateRange(String debtorId, LocalDate startDate, LocalDate endDate) {
            return saved.stream().filter(d -> d.getDebtorId().equals(debtorId)).toList();
        }
    }
}
