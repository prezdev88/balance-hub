package cl.prezdev.balancehub.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import cl.prezdev.balancehub.domain.enums.HouseholdBagMovementType;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class HouseholdBagMovement {

    private final String id;
    private final String bagId;
    private final BigDecimal amount;
    private final HouseholdBagMovementType movementType;
    private final Instant createdAt;

    public HouseholdBagMovement(
        String bagId,
        BigDecimal amount,
        HouseholdBagMovementType movementType
    ) {
        this(UUID.randomUUID().toString(), bagId, amount, movementType, Instant.now());
    }

    public HouseholdBagMovement(
        String id,
        String bagId,
        BigDecimal amount,
        HouseholdBagMovementType movementType,
        Instant createdAt
    ) {
        validate(id, bagId, amount, movementType, createdAt);
        this.id = id;
        this.bagId = bagId;
        this.amount = amount;
        this.movementType = movementType;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getBagId() {
        return bagId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public HouseholdBagMovementType getMovementType() {
        return movementType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    private static void validate(
        String id,
        String bagId,
        BigDecimal amount,
        HouseholdBagMovementType movementType,
        Instant createdAt
    ) {
        if (id == null || id.isBlank()) {
            throw new InvalidHouseholdBudgetException("Movement ID cannot be null or blank");
        }

        if (bagId == null || bagId.isBlank()) {
            throw new InvalidHouseholdBudgetException("Bag ID cannot be null or blank");
        }

        if (amount == null) {
            throw new InvalidHouseholdBudgetException("Movement amount cannot be null");
        }

        if (movementType == null) {
            throw new InvalidHouseholdBudgetException("Movement type cannot be null");
        }

        if (createdAt == null) {
            throw new InvalidHouseholdBudgetException("CreatedAt cannot be null");
        }
    }
}
