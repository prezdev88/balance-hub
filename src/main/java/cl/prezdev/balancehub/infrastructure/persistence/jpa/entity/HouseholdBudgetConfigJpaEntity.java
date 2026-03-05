package cl.prezdev.balancehub.infrastructure.persistence.jpa.entity;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "household_budget_configs")
public class HouseholdBudgetConfigJpaEntity {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private HouseholdBudgetCategory category;

    @Column(name = "monthly_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal monthlyAmount;

    @Column(name = "remaining_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal remainingAmount;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected HouseholdBudgetConfigJpaEntity() {}

    public HouseholdBudgetConfigJpaEntity(
        HouseholdBudgetCategory category,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {
        this.category = category;
        this.monthlyAmount = monthlyAmount;
        this.remainingAmount = remainingAmount;
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
}
