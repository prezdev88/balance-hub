package cl.prezdev.balancehub.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.domain.exception.InvalidDebtorException;

class DebtorTest {

    @Test
    void shouldThrowWhenIdIsNullInFullConstructor() {
        assertThrows(InvalidDebtorException.class, () -> new Debtor(null, "Name", "a@b.com"));
    }

    @Test
    void shouldThrowWhenIdIsBlankInFullConstructor() {
        assertThrows(InvalidDebtorException.class, () -> new Debtor(" ", "Name", "a@b.com"));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(InvalidDebtorException.class, () -> new Debtor(null, "a@b.com"));
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThrows(InvalidDebtorException.class, () -> new Debtor(" ", "a@b.com"));
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        assertThrows(InvalidDebtorException.class, () -> new Debtor("Name", null));
    }

    @Test
    void shouldThrowWhenEmailIsBlank() {
        assertThrows(InvalidDebtorException.class, () -> new Debtor("Name", " "));
    }

    @Test
    void shouldCreateDebtorWithGeneratedId() {
        var debtor = new Debtor("John", "john@doe.com");

        assertNotNull(debtor.getId());
        assertEquals("John", debtor.getName());
        assertEquals("john@doe.com", debtor.getEmail());
    }

    @Test
    void shouldCreateDebtorWithExplicitId() {
        var debtor = new Debtor("debtor-1", "Jane", "jane@doe.com");

        assertEquals("debtor-1", debtor.getId());
        assertEquals("Jane", debtor.getName());
        assertEquals("jane@doe.com", debtor.getEmail());
    }
}
