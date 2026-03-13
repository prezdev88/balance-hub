package cl.prezdev.balancehub.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.domain.exception.InvalidPendingException;

class PendingTest {

    @Test
    void shouldThrowWhenIdIsInvalid() {
        assertThrows(InvalidPendingException.class, () -> new Pending(null, "Transferir", Instant.now()));
        assertThrows(InvalidPendingException.class, () -> new Pending("", "Transferir", Instant.now()));
    }

    @Test
    void shouldThrowWhenDescriptionIsInvalid() {
        assertThrows(InvalidPendingException.class, () -> new Pending("id", null, Instant.now()));
        assertThrows(InvalidPendingException.class, () -> new Pending("id", " ", Instant.now()));
    }

    @Test
    void shouldThrowWhenCreatedAtIsNull() {
        assertThrows(InvalidPendingException.class, () -> new Pending("id", "Transferir", null));
    }

    @Test
    void shouldCreatePendingWithGeneratedValues() {
        Pending pending = new Pending("Transferir compra del dia");

        assertNotNull(pending.getId());
        assertNotNull(pending.getCreatedAt());
        assertEquals("Transferir compra del dia", pending.getDescription());
    }
}
