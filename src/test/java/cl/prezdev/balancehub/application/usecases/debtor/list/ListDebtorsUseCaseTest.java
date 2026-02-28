package cl.prezdev.balancehub.application.usecases.debtor.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.DebtorAccessRepository;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.domain.Debtor;

class ListDebtorsUseCaseTest {

    private ListDebtorsUseCase useCase;
    private InMemoryDebtorRepository repo;
    private InMemoryDebtRepository debtRepo;
    private InMemoryDebtorAccessRepository debtorAccessRepo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryDebtorRepository();
        debtRepo = new InMemoryDebtRepository();
        debtorAccessRepo = new InMemoryDebtorAccessRepository();
        useCase = new ListDebtorsUseCase(repo, debtRepo, debtorAccessRepo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListDebtorsUseCase(null, debtRepo, debtorAccessRepo));
    }

    @Test
    void shouldThrowWhenDebtRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListDebtorsUseCase(repo, null, debtorAccessRepo));
    }

    @Test
    void shouldThrowWhenDebtorAccessRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListDebtorsUseCase(repo, debtRepo, null));
    }

    @Test
    void shouldReturnMappedDebtors() {
        repo.saved.add(new Debtor("Alice", "alice@example.com"));
        repo.saved.add(new Debtor("Bob", "bob@example.com"));
        debtRepo.totalsByDebtorId.add(BigDecimal.valueOf(1200));
        debtRepo.totalsByDebtorId.add(BigDecimal.valueOf(500));
        debtorAccessRepo.canLoginByDebtorIndex.add(true);
        debtorAccessRepo.canLoginByDebtorIndex.add(false);

        var result = useCase.execute();

        assertEquals(2, result.debtors().size());
        assertEquals("Alice", result.debtors().get(0).name());
        assertEquals("bob@example.com", result.debtors().get(1).email());
        assertEquals(0, result.debtors().get(0).totalDebt().compareTo(BigDecimal.valueOf(1200)));
        assertEquals(true, result.debtors().get(0).accessEnabled());
        assertEquals(false, result.debtors().get(1).accessEnabled());
    }

    static class InMemoryDebtorRepository implements DebtorRepository {

        List<Debtor> saved = new ArrayList<>();

        @Override
        public void save(Debtor debtor) {
            saved.add(debtor);
        }

        @Override
        public List<Debtor> findAll() {
            return List.copyOf(saved);
        }

        @Override
        public Optional<Debtor> findById(String id) {
            return saved.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
        }
    }

    static class InMemoryDebtRepository implements DebtRepository {
        List<BigDecimal> totalsByDebtorId = new ArrayList<>();
        int readIndex;

        @Override
        public void save(cl.prezdev.balancehub.domain.Debt debt) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<cl.prezdev.balancehub.domain.Debt> findById(String debtId) {
            return Optional.empty();
        }

        @Override
        public void deleteById(String debtId) {
            // not needed in this test
        }

        @Override
        public BigDecimal totalByDebtorId(String debtorId) {
            if (readIndex < totalsByDebtorId.size()) {
                return totalsByDebtorId.get(readIndex++);
            }
            return BigDecimal.ZERO;
        }

        @Override
        public List<cl.prezdev.balancehub.domain.Debt> findByDebtorIdAndDateRange(
            String debtorId,
            LocalDate startDate,
            LocalDate endDate
        ) {
            throw new UnsupportedOperationException();
        }
    }

    static class InMemoryDebtorAccessRepository implements DebtorAccessRepository {
        List<Boolean> canLoginByDebtorIndex = new ArrayList<>();
        int readIndex;

        @Override
        public boolean canLogin(String debtorId) {
            if (readIndex < canLoginByDebtorIndex.size()) {
                return canLoginByDebtorIndex.get(readIndex++);
            }
            return false;
        }
    }
}
