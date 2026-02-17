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

    public Debt(String id, String description, BigDecimal amount, String debtorId, Instant createdAt, boolean settled) {
        validate(id, description, amount, debtorId, createdAt);

        this.id = id;
        this.description = description;
        this.totalAmount = amount;
        this.debtorId = debtorId;
        this.createdAt = createdAt;
        this.settled = settled;
    }

    private static void validate(String id, String description, BigDecimal amount, String debtorId, Instant createdAt) {
        if (id == null || id.isBlank()) {
            throw new InvalidDebtException("Debt ID cannot be null or blank");
        }

        if (description == null || description.isBlank()) {
            throw new InvalidDebtException("Debt description cannot be null or blank");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDebtException("Debt amount must be greater than zero");
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
}
