package cl.prezdev.balancehub.application.usecases.pending.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.PendingRepository;
import cl.prezdev.balancehub.domain.Pending;

class CreatePendingUseCaseTest {

    private CreatePendingUseCase useCase;
    private InMemoryPendingRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPendingRepository();
        useCase = new CreatePendingUseCase(repository);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CreatePendingUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldSavePending() {
        CreatePendingResult result = useCase.execute(new CreatePendingCommand("Transferir compra farmacia"));

        assertNotNull(result.pendingId());
        assertNotNull(result.createdAt());
        assertEquals("Transferir compra farmacia", result.description());
        assertEquals(1, repository.saved.size());
        assertEquals("Transferir compra farmacia", repository.saved.getFirst().getDescription());
    }

    static class InMemoryPendingRepository implements PendingRepository {
        List<Pending> saved = new ArrayList<>();

        @Override
        public void save(Pending pending) {
            saved.add(pending);
        }

        @Override
        public List<Pending> findAll() {
            return List.copyOf(saved);
        }

        @Override
        public Optional<Pending> findById(String id) {
            return saved.stream().filter(pending -> pending.getId().equals(id)).findFirst();
        }

        @Override
        public void deleteById(String id) {
            saved.removeIf(pending -> pending.getId().equals(id));
        }
    }
}
