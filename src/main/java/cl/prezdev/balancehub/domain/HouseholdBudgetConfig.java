package cl.prezdev.balancehub.domain;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class HouseholdBudgetConfig {

    private static final String VEGETABLES_BAG_ID = "f7f4d9d1-15d8-4f2c-9c8f-5f6a96f9c101";
    private static final String GROCERIES_BAG_ID = "c1b71f59-9ea8-4f0d-9153-f5b6a3b21d02";
    private static final String VEGETABLES_BAG_NAME = "Verduras";
    private static final String GROCERIES_BAG_NAME = "Mercadería";
    private static final String VEGETABLES_BAG_EMOJI = "🥦";
    private static final String GROCERIES_BAG_EMOJI = "🛒";

    private final String id;
    private final String name;
    private final String emoji;
    private BigDecimal monthlyAmount;
    private BigDecimal remainingAmount;
    private Instant updatedAt;

    public HouseholdBudgetConfig(HouseholdBudgetCategory category, BigDecimal monthlyAmount) {
        this(
            getCompatibilityId(category),
            getCompatibilityName(category),
            getCompatibilityEmoji(category),
            monthlyAmount,
            monthlyAmount,
            Instant.now()
        );
    }

    public HouseholdBudgetConfig(
        String id,
        String name,
        String emoji,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {
        validate(id, name, emoji, monthlyAmount, remainingAmount, updatedAt);
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.monthlyAmount = monthlyAmount;
        this.remainingAmount = remainingAmount;
        this.updatedAt = updatedAt;
    }

    public void updateMonthlyAmount(BigDecimal monthlyAmount, Instant updatedAt) {
        validate(id, name, emoji, monthlyAmount, monthlyAmount, updatedAt);
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmoji() {
        return emoji;
    }

    public HouseholdBudgetCategory getCategory() {
        if (VEGETABLES_BAG_NAME.equals(name)) {
            return HouseholdBudgetCategory.VEGETABLES;
        }
        if (GROCERIES_BAG_NAME.equals(name)) {
            return HouseholdBudgetCategory.GROCERIES;
        }
        throw new InvalidHouseholdBudgetException("Bag " + name + " has no compatibility category");
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
        String id,
        String name,
        String emoji,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {
        if (id == null || id.isBlank()) {
            throw new InvalidHouseholdBudgetException("Bag ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidHouseholdBudgetException("Bag name cannot be null or blank");
        }
        if (emoji == null || emoji.isBlank()) {
            throw new InvalidHouseholdBudgetException("Bag emoji cannot be null or blank");
        }
        if (monthlyAmount == null) {
            throw new InvalidHouseholdBudgetException("Monthly amount cannot be null");
        }
        if (monthlyAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidHouseholdBudgetException("Monthly amount cannot be negative");
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

    private static String getCompatibilityId(HouseholdBudgetCategory category) {
        if (category == null) {
            throw new InvalidHouseholdBudgetException("Category cannot be null");
        }
        return switch (category) {
            case VEGETABLES -> VEGETABLES_BAG_ID;
            case GROCERIES -> GROCERIES_BAG_ID;
        };
    }

    private static String getCompatibilityName(HouseholdBudgetCategory category) {
        if (category == null) {
            throw new InvalidHouseholdBudgetException("Category cannot be null");
        }
        return switch (category) {
            case VEGETABLES -> VEGETABLES_BAG_NAME;
            case GROCERIES -> GROCERIES_BAG_NAME;
        };
    }

    private static String getCompatibilityEmoji(HouseholdBudgetCategory category) {
        if (category == null) {
            throw new InvalidHouseholdBudgetException("Category cannot be null");
        }
        return switch (category) {
            case VEGETABLES -> VEGETABLES_BAG_EMOJI;
            case GROCERIES -> GROCERIES_BAG_EMOJI;
        };
    }
}
