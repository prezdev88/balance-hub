package cl.prezdev.balancehub.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import cl.prezdev.balancehub.domain.exception.InvalidDebtException;

public class Debt {

    private final String id;
    private final String description;
    private final BigDecimal totalAmount;
    private final String debtorId;
    private final Instant createdAt;
    private final boolean settled;

    public Debt(String description, BigDecimal totalAmount, String debtorId) {
        this(UUID.randomUUID().toString(), description, totalAmount, debtorId, Instant.now(), false);
    }

    public Debt(String id, String description, BigDecimal totalAmount, String debtorId, Instant createdAt, boolean settled) {
        validate(id, description, totalAmount, debtorId, createdAt);

        this.id = id;
        this.description = description;
        this.totalAmount = totalAmount;
        this.debtorId = debtorId;
        this.createdAt = createdAt;
        this.settled = settled;
    }

    private static void validate(String id, String description, BigDecimal totalAmount, String debtorId, Instant createdAt) {
        if (id == null || id.isBlank()) {
            throw new InvalidDebtException("Debt ID cannot be null or blank");
        }

        if (description == null || description.isBlank()) {
            throw new InvalidDebtException("Debt description cannot be null or blank");
        }

        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDebtException("Debt total amount must be greater than zero");
        }

        if (debtorId == null || debtorId.isBlank()) {
            throw new InvalidDebtException("Debtor ID cannot be null or blank");
        }

        if (createdAt == null) {
            throw new InvalidDebtException("Creation date cannot be null");
        }
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getDebtorId() {
        return debtorId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isSettled() {
        return settled;
    }

    public Debt withDescription(String description) {
        return new Debt(this.id, description, this.totalAmount, this.debtorId, this.createdAt, this.settled);
    }

    public Debt withTotalAmount(BigDecimal totalAmount) {
        return new Debt(this.id, this.description, totalAmount, this.debtorId, this.createdAt, this.settled);
    }

    public Debt withCreatedAt(Instant createdAt) {
        return new Debt(this.id, this.description, this.totalAmount, this.debtorId, createdAt, this.settled);
    }

    public Debt withSettled(boolean settled) {
        return new Debt(this.id, this.description, this.totalAmount, this.debtorId, this.createdAt, settled);
    }
}
