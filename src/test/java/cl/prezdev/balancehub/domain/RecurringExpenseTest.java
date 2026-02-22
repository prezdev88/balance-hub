package cl.prezdev.balancehub.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.domain.enums.ExpenseType;
import cl.prezdev.balancehub.domain.exception.InvalidRecurringExpenseException;

class RecurringExpenseTest {

    @Test
    void shouldThrowWhenIdIsNull() {
        assertThrows(InvalidRecurringExpenseException.class,
            () -> new RecurringExpense(null, "desc", BigDecimal.TEN, ExpenseType.FIXED));
    }

    @Test
    void shouldThrowWhenIdIsBlank() {
        assertThrows(InvalidRecurringExpenseException.class,
            () -> new RecurringExpense("", "desc", BigDecimal.TEN, ExpenseType.FIXED));
    }

    @Test
    void shouldThrowWhenDescriptionIsNull() {
        assertThrows(InvalidRecurringExpenseException.class,
            () -> new RecurringExpense("id", null, BigDecimal.TEN, ExpenseType.FIXED));
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        assertThrows(InvalidRecurringExpenseException.class,
            () -> new RecurringExpense("id", "desc", null, ExpenseType.FIXED));
    }

    @Test
    void shouldThrowWhenAmountIsNotPositive() {
        assertThrows(InvalidRecurringExpenseException.class,
            () -> new RecurringExpense("id", "desc", BigDecimal.ZERO, ExpenseType.FIXED));
    }

    @Test
    void shouldThrowWhenTypeIsNull() {
        assertThrows(InvalidRecurringExpenseException.class,
            () -> new RecurringExpense("id", "desc", BigDecimal.TEN, null));
    }

    @Test
    void setAmountAndDescriptionValidation() {
        var r = new RecurringExpense("id", "desc", BigDecimal.TEN, ExpenseType.FIXED);

        assertThrows(InvalidRecurringExpenseException.class, () -> r.setAmount(null));
        assertThrows(InvalidRecurringExpenseException.class, () -> r.setAmount(BigDecimal.ZERO));
        assertThrows(InvalidRecurringExpenseException.class, () -> r.setDescription(null));
        assertThrows(InvalidRecurringExpenseException.class, () -> r.setDescription(""));
    }

    @Test
    void typeChecks() {
        var fixed = new RecurringExpense("id", "d", BigDecimal.TEN, ExpenseType.FIXED);
        var optional = new RecurringExpense("id2", "d2", BigDecimal.valueOf(5), ExpenseType.OPTIONAL);

        assertTrue(fixed.isFixed());
        assertFalse(fixed.isOptional());

        assertTrue(optional.isOptional());
        assertFalse(optional.isFixed());

        assertEquals("d", fixed.getDescription());
        assertEquals(0, fixed.getAmount().compareTo(BigDecimal.TEN));
    }

    @Test
    void shouldCreateWithGeneratedIdAndAllowValidUpdates() {
        var recurring = new RecurringExpense("Gym", BigDecimal.valueOf(20), ExpenseType.OPTIONAL);

        assertNotNull(recurring.getId());

        recurring.setDescription("Gym Premium");
        recurring.setAmount(BigDecimal.valueOf(25));

        assertEquals("Gym Premium", recurring.getDescription());
        assertEquals(0, recurring.getAmount().compareTo(BigDecimal.valueOf(25)));
        assertEquals(ExpenseType.OPTIONAL, recurring.getType());
    }
}
