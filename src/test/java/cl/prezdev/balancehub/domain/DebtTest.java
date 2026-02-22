package cl.prezdev.balancehub.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.domain.exception.InvalidDebtException;

class DebtTest {

    @Test
    void shouldThrowWhenIdIsNull() {
        assertThrows(InvalidDebtException.class,
            () -> new Debt(null, "Debt", BigDecimal.TEN, "debtor-1", Instant.now(), false));
    }

    @Test
    void shouldThrowWhenIdIsBlank() {
        assertThrows(InvalidDebtException.class,
            () -> new Debt(" ", "Debt", BigDecimal.TEN, "debtor-1", Instant.now(), false));
    }

    @Test
    void shouldThrowWhenDescriptionIsNull() {
        assertThrows(InvalidDebtException.class,
            () -> new Debt("id", null, BigDecimal.TEN, "debtor-1", Instant.now(), false));
    }

    @Test
    void shouldThrowWhenDescriptionIsBlank() {
        assertThrows(InvalidDebtException.class,
            () -> new Debt("id", " ", BigDecimal.TEN, "debtor-1", Instant.now(), false));
    }

    @Test
    void shouldThrowWhenAmountIsNullOrNotPositive() {
        assertThrows(InvalidDebtException.class,
            () -> new Debt("id", "Debt", null, "debtor-1", Instant.now(), false));
        assertThrows(InvalidDebtException.class,
            () -> new Debt("id", "Debt", BigDecimal.ZERO, "debtor-1", Instant.now(), false));
    }

    @Test
    void shouldThrowWhenDebtorIdIsBlank() {
        assertThrows(InvalidDebtException.class,
            () -> new Debt("id", "Debt", BigDecimal.TEN, "", Instant.now(), false));
    }

    @Test
    void shouldThrowWhenDebtorIdIsNull() {
        assertThrows(InvalidDebtException.class,
            () -> new Debt("id", "Debt", BigDecimal.TEN, null, Instant.now(), false));
    }

    @Test
    void shouldThrowWhenCreatedAtIsNull() {
        assertThrows(InvalidDebtException.class,
            () -> new Debt("id", "Debt", BigDecimal.TEN, "debtor-1", null, false));
    }

    @Test
    void shouldCreateDebtWithDefaultsInShortConstructor() {
        var debt = new Debt("Laptop", BigDecimal.valueOf(1200), "debtor-1");

        assertNotNull(debt.getId());
        assertEquals("Laptop", debt.getDescription());
        assertEquals(0, debt.getTotalAmount().compareTo(BigDecimal.valueOf(1200)));
        assertEquals("debtor-1", debt.getDebtorId());
        assertNotNull(debt.getCreatedAt());
        assertFalse(debt.isSettled());
    }

    @Test
    void shouldKeepProvidedValuesInFullConstructor() {
        var createdAt = Instant.parse("2026-02-22T12:00:00Z");
        var debt = new Debt("debt-1", "TV", BigDecimal.valueOf(500), "debtor-1", createdAt, true);

        assertEquals("debt-1", debt.getId());
        assertEquals("TV", debt.getDescription());
        assertEquals(0, debt.getTotalAmount().compareTo(BigDecimal.valueOf(500)));
        assertEquals("debtor-1", debt.getDebtorId());
        assertEquals(createdAt, debt.getCreatedAt());
        assertEquals(true, debt.isSettled());
    }
}
