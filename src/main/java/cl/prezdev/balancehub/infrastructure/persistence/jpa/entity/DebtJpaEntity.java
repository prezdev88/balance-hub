package cl.prezdev.balancehub.infrastructure.persistence.jpa.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "debts")
public class DebtJpaEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "debtor_id", nullable = false, length = 64)
    private String debtorId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "settled", nullable = false)
    private boolean settled;

    protected DebtJpaEntity() {
    }

    public DebtJpaEntity(String id, String description, BigDecimal totalAmount, String debtorId, Instant createdAt, boolean settled) {
        this.id = id;
        this.description = description;
        this.totalAmount = totalAmount;
        this.debtorId = debtorId;
        this.createdAt = createdAt;
        this.settled = settled;
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
