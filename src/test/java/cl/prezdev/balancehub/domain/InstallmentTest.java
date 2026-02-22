package cl.prezdev.balancehub.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.domain.exception.InvalidInstallmentException;

class InstallmentTest {

    @Test
    void shouldThrowWhenIdIsNull() {
        assertThrows(InvalidInstallmentException.class,
            () -> new Installment(null, "debt-1", 1, LocalDate.now(), BigDecimal.TEN, null));
    }

    @Test
    void shouldThrowWhenIdIsBlank() {
        assertThrows(InvalidInstallmentException.class,
            () -> new Installment(" ", "debt-1", 1, LocalDate.now(), BigDecimal.TEN, null));
    }

    @Test
    void shouldThrowWhenDebtIdIsNull() {
        assertThrows(InvalidInstallmentException.class,
            () -> new Installment("id", null, 1, LocalDate.now(), BigDecimal.TEN, null));
    }

    @Test
    void shouldThrowWhenDebtIdIsBlank() {
        assertThrows(InvalidInstallmentException.class,
            () -> new Installment("id", " ", 1, LocalDate.now(), BigDecimal.TEN, null));
    }

    @Test
    void shouldThrowWhenNumberIsNotPositive() {
        assertThrows(InvalidInstallmentException.class,
            () -> new Installment("id", "debt-1", 0, LocalDate.now(), BigDecimal.TEN, null));
    }

    @Test
    void shouldThrowWhenDueDateIsNull() {
        assertThrows(InvalidInstallmentException.class,
            () -> new Installment("id", "debt-1", 1, null, BigDecimal.TEN, null));
    }

    @Test
    void shouldThrowWhenAmountIsNullOrNotPositive() {
        assertThrows(InvalidInstallmentException.class,
            () -> new Installment("id", "debt-1", 1, LocalDate.now(), null, null));
        assertThrows(InvalidInstallmentException.class,
            () -> new Installment("id", "debt-1", 1, LocalDate.now(), BigDecimal.ZERO, null));
    }

    @Test
    void shouldCreateUnpaidInstallmentWithShortConstructor() {
        var installment = new Installment("debt-1", 1, LocalDate.of(2026, 3, 10), BigDecimal.valueOf(150));

        assertNotNull(installment.getId());
        assertEquals("debt-1", installment.getDebtId());
        assertEquals(1, installment.getNumber());
        assertEquals(LocalDate.of(2026, 3, 10), installment.getDueDate());
        assertEquals(0, installment.getAmount().compareTo(BigDecimal.valueOf(150)));
        assertFalse(installment.isPaid());
    }

    @Test
    void shouldPayInstallmentOnce() {
        var installment = new Installment("debt-1", 1, LocalDate.now(), BigDecimal.TEN);
        var paidAt = Instant.now();

        installment.pay(paidAt);

        assertTrue(installment.isPaid());
        assertEquals(paidAt, installment.getPaidAt());
    }

    @Test
    void shouldThrowWhenPayingWithNullDateOrAlreadyPaid() {
        var installment = new Installment("debt-1", 1, LocalDate.now(), BigDecimal.TEN);

        assertThrows(InvalidInstallmentException.class, () -> installment.pay(null));

        installment.pay(Instant.now());
        assertThrows(InvalidInstallmentException.class, () -> installment.pay(Instant.now()));
    }

    @Test
    void fullConstructorShouldKeepPaidInstallmentState() {
        var paidAt = Instant.parse("2026-02-22T10:00:00Z");
        var installment = new Installment(
            "inst-1",
            "debt-1",
            2,
            LocalDate.of(2026, 4, 10),
            BigDecimal.valueOf(300),
            paidAt
        );

        assertEquals("inst-1", installment.getId());
        assertEquals("debt-1", installment.getDebtId());
        assertEquals(2, installment.getNumber());
        assertEquals(LocalDate.of(2026, 4, 10), installment.getDueDate());
        assertEquals(0, installment.getAmount().compareTo(BigDecimal.valueOf(300)));
        assertTrue(installment.isPaid());
        assertEquals(paidAt, installment.getPaidAt());
    }
}
