package cl.prezdev.balancehub.application.usecases.debtor.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.domain.Debtor;
import cl.prezdev.balancehub.domain.exception.InvalidDebtorException;

class CreateDebtorUseCaseTest {
    
    private CreateDebtorUseCase useCase;
    private InMemoryDebtorRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryDebtorRepository();
        useCase = new CreateDebtorUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CreateDebtorUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldReturnDebtorIdWhenCreated() {
        var result = useCase.execute(new CreateDebtorCommand("John Doe", "john@doe.com"));

        assertNotNull(result);
        assertNotNull(result.debtorId());
        assertFalse(result.debtorId().isBlank());
    }

    @Test
    void shouldSaveDebtorWithNameAndEmail() {
        useCase.execute(new CreateDebtorCommand("Jane Doe", "jane@doe.com"));

        assertNotNull(repo.lastSaved);
        assertEquals("Jane Doe", repo.lastSaved.getName());
        assertEquals("jane@doe.com", repo.lastSaved.getEmail());
        assertEquals(1, repo.saved.size());
    }

    @Test
    void shouldThrowWhenNameIsInvalid() {
        assertThrows(InvalidDebtorException.class, () -> useCase.execute(new CreateDebtorCommand("", "e@e.com")));
    }

    @Test
    void shouldThrowWhenEmailIsInvalid() {
        assertThrows(InvalidDebtorException.class, () -> useCase.execute(new CreateDebtorCommand("Name", "")));
    }

    static class InMemoryDebtorRepository implements DebtorRepository {

        Debtor lastSaved;
        List<Debtor> saved = new ArrayList<>();

        @Override
        public void save(Debtor debtor) {
            lastSaved = debtor;
            saved.add(debtor);
        }

        @Override
        public List<Debtor> findAll() {
            return List.copyOf(saved);
        }
    }
}
