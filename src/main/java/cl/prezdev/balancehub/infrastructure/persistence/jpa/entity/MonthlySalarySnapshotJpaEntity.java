package cl.prezdev.balancehub.infrastructure.persistence.jpa.entity;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.SalarySnapshotStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "monthly_salary_snapshots")
public class MonthlySalarySnapshotJpaEntity {

    @Id
    @Column(length = 64, nullable = false)
    private String id;

    @Column(name = "debtor_id", length = 64, nullable = false)
    private String debtorId;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(name = "monthly_free_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal monthlyFreeAmount;

    @Column(name = "half_free_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal halfFreeAmount;

    @Column(name = "total_installments_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalInstallmentsAmount;

    @Column(name = "salary_column_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal salaryColumnAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SalarySnapshotStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    protected MonthlySalarySnapshotJpaEntity() {}

    public MonthlySalarySnapshotJpaEntity(
        String id,
        String debtorId,
        int year,
        int month,
        BigDecimal monthlyFreeAmount,
        BigDecimal halfFreeAmount,
        BigDecimal totalInstallmentsAmount,
        BigDecimal salaryColumnAmount,
        SalarySnapshotStatus status,
        Instant createdAt,
        Instant paidAt
    ) {
        this.id = id;
        this.debtorId = debtorId;
        this.year = year;
        this.month = month;
        this.monthlyFreeAmount = monthlyFreeAmount;
        this.halfFreeAmount = halfFreeAmount;
        this.totalInstallmentsAmount = totalInstallmentsAmount;
        this.salaryColumnAmount = salaryColumnAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }

    public String getId() {
        return id;
    }

    public String getDebtorId() {
        return debtorId;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public BigDecimal getMonthlyFreeAmount() {
        return monthlyFreeAmount;
    }

    public BigDecimal getHalfFreeAmount() {
        return halfFreeAmount;
    }

    public BigDecimal getTotalInstallmentsAmount() {
        return totalInstallmentsAmount;
    }

    public BigDecimal getSalaryColumnAmount() {
        return salaryColumnAmount;
    }

    public SalarySnapshotStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getPaidAt() {
        return paidAt;
    }
}
