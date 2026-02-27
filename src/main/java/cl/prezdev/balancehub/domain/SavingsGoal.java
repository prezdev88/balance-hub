package cl.prezdev.balancehub.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import cl.prezdev.balancehub.domain.exception.InvalidSavingsGoalException;

public class SavingsGoal {

    private final String id;
    private final BigDecimal amount;
    private final Instant createdAt;
    private final boolean active;

    public SavingsGoal(BigDecimal amount) {
        this(UUID.randomUUID().toString(), amount, Instant.now(), true);
    }

    public SavingsGoal(String id, BigDecimal amount, Instant createdAt, boolean active) {
        validate(id, amount, createdAt);
        this.id = id;
        this.amount = amount;
        this.createdAt = createdAt;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    private static void validate(String id, BigDecimal amount, Instant createdAt) {
        if (id == null || id.isBlank()) {
            throw new InvalidSavingsGoalException("Savings goal id cannot be null or blank");
        }
        if (amount == null) {
            throw new InvalidSavingsGoalException("Savings goal amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSavingsGoalException("Savings goal amount must be greater than zero");
        }
        if (createdAt == null) {
            throw new InvalidSavingsGoalException("Savings goal creation date cannot be null");
        }
    }
}
