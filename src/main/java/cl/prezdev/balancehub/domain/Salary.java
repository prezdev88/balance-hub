package cl.prezdev.balancehub.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import cl.prezdev.balancehub.domain.exception.InvalidSalaryException;

public class Salary {

    private final String id;
    private final BigDecimal amount;
    private final Instant createdAt;
    private final boolean active;

    public Salary(BigDecimal amount) {
        this(
            UUID.randomUUID().toString(), 
            amount, 
            Instant.now(), 
            true
        );
    }

    public Salary(String id, BigDecimal amount, Instant createdAt, boolean active) {
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
            throw new InvalidSalaryException("Salary id cannot be null or blank");
        }

        if (amount == null) {
            throw new InvalidSalaryException("Salary amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSalaryException("Salary amount must be greater than zero");
        }

        if (createdAt == null) {
            throw new InvalidSalaryException("Salary creation date cannot be null");
        }
    }
}
