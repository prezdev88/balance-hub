package cl.prezdev.balancehub.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.domain.exception.InvalidDebtorException;

class DebtorTest {

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(InvalidDebtorException.class, () -> new Debtor(null, "a@b.com"));
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        assertThrows(InvalidDebtorException.class, () -> new Debtor("Name", null));
    }
}
