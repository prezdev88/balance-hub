package cl.prezdev.balancehub.domain;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class HouseholdBudgetConfig {

    private final HouseholdBudgetCategory category;
    private BigDecimal monthlyAmount;
    private BigDecimal remainingAmount;
    private Instant updatedAt;

    public HouseholdBudgetConfig(HouseholdBudgetCategory category, BigDecimal monthlyAmount) {
        this(category, monthlyAmount, monthlyAmount, Instant.now());
    }

    public HouseholdBudgetConfig(
        HouseholdBudgetCategory category,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {
        validate(category, monthlyAmount, remainingAmount, updatedAt);
        this.category = category;
        this.monthlyAmount = monthlyAmount;
        this.remainingAmount = remainingAmount;
        this.updatedAt = updatedAt;
    }

    public void updateMonthlyAmount(BigDecimal monthlyAmount, Instant updatedAt) {
        validate(category, monthlyAmount, monthlyAmount, updatedAt);
        this.monthlyAmount = monthlyAmount;
        this.remainingAmount = monthlyAmount;
        this.updatedAt = updatedAt;
    }

    public void consume(BigDecimal amount, Instant updatedAt) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidHouseholdBudgetException("Amount must be different from zero");
        }
        if (updatedAt == null) {
            throw new InvalidHouseholdBudgetException("UpdatedAt cannot be null");
        }
        BigDecimal newRemainingAmount = this.remainingAmount.subtract(amount);
        if (newRemainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidHouseholdBudgetException("Expense exceeds remaining amount");
        }
        if (newRemainingAmount.compareTo(this.monthlyAmount) > 0) {
            throw new InvalidHouseholdBudgetException("Adjustment exceeds already spent amount");
        }
        this.remainingAmount = newRemainingAmount;
        this.updatedAt = updatedAt;
    }

    public void reset(Instant updatedAt) {
        if (updatedAt == null) {
            throw new InvalidHouseholdBudgetException("UpdatedAt cannot be null");
        }
        this.remainingAmount = this.monthlyAmount;
        this.updatedAt = updatedAt;
    }

    public HouseholdBudgetCategory getCategory() {
        return category;
    }

    public BigDecimal getMonthlyAmount() {
        return monthlyAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private static void validate(
        HouseholdBudgetCategory category,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {
        if (category == null) {
            throw new InvalidHouseholdBudgetException("Category cannot be null");
        }
        if (monthlyAmount == null) {
            throw new InvalidHouseholdBudgetException("Monthly amount cannot be null");
        }
        if (monthlyAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidHouseholdBudgetException("Monthly amount must be greater than zero");
        }
        if (remainingAmount == null) {
            throw new InvalidHouseholdBudgetException("Remaining amount cannot be null");
        }
        if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidHouseholdBudgetException("Remaining amount cannot be negative");
        }
        if (updatedAt == null) {
            throw new InvalidHouseholdBudgetException("UpdatedAt cannot be null");
        }
    }
}
