package cl.prezdev.balancehub.infrastructure.persistence.jpa.entity;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.HouseholdBagMovementType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "household_bag_movements")
public class HouseholdBagMovementJpaEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "bag_id", nullable = false, length = 64)
    private String bagId;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private HouseholdBagMovementType movementType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected HouseholdBagMovementJpaEntity() {}

    public HouseholdBagMovementJpaEntity(
        String id,
        String bagId,
        BigDecimal amount,
        HouseholdBagMovementType movementType,
        Instant createdAt
    ) {
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
}
