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
        var amount = BigDecimal.valueOf(1000);
        var createdAt = Instant.now();
        assertThrows(InvalidSalaryException.class,
            () -> new Salary(null, amount, createdAt, true));
    }

    @Test
    void shouldThrowWhenIdIsBlank() {
        var amount = BigDecimal.valueOf(1000);
        var createdAt = Instant.now();
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("", amount, createdAt, true));
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        var createdAt = Instant.now();
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("id", null, createdAt, true));
    }

    @Test
    void shouldThrowWhenAmountIsNotPositive() {
        var createdAt = Instant.now();
        var negative = BigDecimal.valueOf(-1);
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("id", BigDecimal.ZERO, createdAt, true));
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("id", negative, createdAt, true));
    }

    @Test
    void shouldThrowWhenCreatedAtIsNull() {
        var amount = BigDecimal.valueOf(1000);
        assertThrows(InvalidSalaryException.class,
            () -> new Salary("id", amount, null, true));
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

    @Test
    void fullConstructorKeepsProvidedValues() {
        var createdAt = Instant.parse("2026-02-22T00:00:00Z");
        var salary = new Salary("salary-1", BigDecimal.valueOf(3000), createdAt, false);

        assertEquals("salary-1", salary.getId());
        assertEquals(0, salary.getAmount().compareTo(BigDecimal.valueOf(3000)));
        assertEquals(createdAt, salary.getCreatedAt());
        assertEquals(false, salary.isActive());
    }
}
