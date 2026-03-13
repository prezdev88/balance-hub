package cl.prezdev.balancehub.application.usecases.pending.delete;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.exception.PendingNotFoundException;
import cl.prezdev.balancehub.application.ports.out.PendingRepository;
import cl.prezdev.balancehub.domain.Pending;

class DeletePendingUseCaseTest {

    private DeletePendingUseCase useCase;
    private InMemoryPendingRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPendingRepository();
        useCase = new DeletePendingUseCase(repository);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new DeletePendingUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldThrowWhenPendingDoesNotExist() {
        assertThrows(PendingNotFoundException.class, () -> useCase.execute(new DeletePendingCommand("missing")));
    }

    @Test
    void shouldDeletePendingWhenExists() {
        repository.saved.add(new Pending("p-1", "Transferir compra", Instant.now()));

        useCase.execute(new DeletePendingCommand("p-1"));

        assertFalse(repository.findById("p-1").isPresent());
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
