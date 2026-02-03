package cl.prezdev.balancehub.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.domain.exception.InvalidSalaryException;

class SalaryTest {

    @Test
    void shouldThrowWhenIdIsNull() {
        assertThrows(InvalidSalaryException.class,
            () -> new Salary(null, BigDecimal.valueOf(1000), Instant.now(), true));
    }

    @Test
    void shouldThrowWhenIdIsBlank() {
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("", BigDecimal.valueOf(1000), Instant.now(), true));
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("id", null, Instant.now(), true));
    }

    @Test
    void shouldThrowWhenAmountIsNotPositive() {
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("id", BigDecimal.ZERO, Instant.now(), true));
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("id", BigDecimal.valueOf(-1), Instant.now(), true));
    }

    @Test
    void shouldThrowWhenCreatedAtIsNull() {
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("id", BigDecimal.valueOf(1000), null, true));
    }

    @Test
    void oneArgConstructorCreatesActiveSalary() {
        var salary = new Salary(BigDecimal.valueOf(2000));

        assertNotNull(salary.getId());
        assertEquals(0, salary.getAmount().compareTo(BigDecimal.valueOf(2000)));
        // createdAt is set to now; just ensure it's not null
        assertNotNull(salary.getCreatedAt());
        assertEquals(true, salary.isActive());
    }
}
