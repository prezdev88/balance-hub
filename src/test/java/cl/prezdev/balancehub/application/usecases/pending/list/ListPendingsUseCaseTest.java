package cl.prezdev.balancehub.application.usecases.pending.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.PendingRepository;
import cl.prezdev.balancehub.domain.Pending;

class ListPendingsUseCaseTest {

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListPendingsUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        ListPendingsUseCase useCase = new ListPendingsUseCase(new StubPendingRepository(List.of()));
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldReturnAllPendings() {
        ListPendingsUseCase useCase = new ListPendingsUseCase(new StubPendingRepository(List.of(
            new Pending("p-1", "Transferir mercado", Instant.parse("2026-03-12T10:15:30Z")),
            new Pending("p-2", "Pagar pedido", Instant.parse("2026-03-12T12:00:00Z"))
        )));

        ListPendingsResult result = useCase.execute(new ListPendingsCommand());

        assertEquals(2, result.pendings().size());
        assertEquals("Transferir mercado", result.pendings().getFirst().description());
        assertEquals("p-2", result.pendings().get(1).id());
    }

    record StubPendingRepository(List<Pending> items) implements PendingRepository {
        @Override
        public void save(Pending pending) {
        }

        @Override
        public List<Pending> findAll() {
            return items;
        }

        @Override
        public Optional<Pending> findById(String id) {
            return items.stream().filter(item -> item.getId().equals(id)).findFirst();
        }

        @Override
        public void deleteById(String id) {
        }
    }
}
