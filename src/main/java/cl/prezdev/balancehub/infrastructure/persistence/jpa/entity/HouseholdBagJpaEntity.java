package cl.prezdev.balancehub.infrastructure.persistence.jpa.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "household_bags")
public class HouseholdBagJpaEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "emoji", nullable = false, length = 32)
    private String emoji;

    @Column(name = "monthly_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal monthlyAmount;

    @Column(name = "remaining_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal remainingAmount;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    protected HouseholdBagJpaEntity() {}

    public HouseholdBagJpaEntity(
        String id,
        String name,
        String emoji,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.monthlyAmount = monthlyAmount;
        this.remainingAmount = remainingAmount;
        this.updatedAt = updatedAt;
        this.active = true;
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

    public BigDecimal getMonthlyAmount() {
        return monthlyAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
