package cl.prezdev.balancehub.infrastructure.persistence.jpa.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "salaries")
public class SalaryJpaEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "active", nullable = false)
    private boolean active;

    protected SalaryJpaEntity() {
    }

    public SalaryJpaEntity(String id, BigDecimal amount, Instant createdAt, boolean active) {
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
}
