package cl.prezdev.balancehub.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

class HouseholdBudgetConfigTest {

    @Test
    void shouldAllowNegativeAmountAsCorrection() {
        HouseholdBudgetConfig config = new HouseholdBudgetConfig(
            HouseholdBudgetCategory.VEGETABLES,
            BigDecimal.valueOf(120000)
        );

        config.consume(BigDecimal.valueOf(30000), Instant.now());
        config.consume(BigDecimal.valueOf(-5000), Instant.now());

        assertEquals(0, config.getRemainingAmount().compareTo(BigDecimal.valueOf(95000)));
    }

    @Test
    void shouldRejectCorrectionThatExceedsAlreadySpentAmount() {
        HouseholdBudgetConfig config = new HouseholdBudgetConfig(
            HouseholdBudgetCategory.GROCERIES,
            BigDecimal.valueOf(300000)
        );

        config.consume(BigDecimal.valueOf(10000), Instant.now());

        assertThrows(
            InvalidHouseholdBudgetException.class,
            () -> config.consume(BigDecimal.valueOf(-20000), Instant.now())
        );
    }

    @Test
    void shouldRejectZeroAmount() {
        HouseholdBudgetConfig config = new HouseholdBudgetConfig(
            HouseholdBudgetCategory.GROCERIES,
            BigDecimal.valueOf(300000)
        );

        assertThrows(
            InvalidHouseholdBudgetException.class,
            () -> config.consume(BigDecimal.ZERO, Instant.now())
        );
    }
}
