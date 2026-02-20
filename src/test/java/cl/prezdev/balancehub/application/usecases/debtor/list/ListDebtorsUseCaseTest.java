package cl.prezdev.balancehub.application.usecases.debtor.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.domain.Debtor;

class ListDebtorsUseCaseTest {

    private ListDebtorsUseCase useCase;
    private InMemoryDebtorRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryDebtorRepository();
        useCase = new ListDebtorsUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListDebtorsUseCase(null));
    }

    @Test
    void shouldReturnMappedDebtors() {
        repo.saved.add(new Debtor("Alice", "alice@example.com"));
        repo.saved.add(new Debtor("Bob", "bob@example.com"));

        var result = useCase.execute();

        assertEquals(2, result.debtors().size());
        assertEquals("Alice", result.debtors().get(0).name());
        assertEquals("bob@example.com", result.debtors().get(1).email());
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
}
