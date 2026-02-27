package cl.prezdev.balancehub.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import cl.prezdev.balancehub.domain.enums.SalarySnapshotStatus;

public class MonthlySalarySnapshot {

    private final String id;
    private final String debtorId;
    private final int year;
    private final int month;
    private final BigDecimal monthlyFreeAmount;
    private final BigDecimal halfFreeAmount;
    private final BigDecimal totalInstallmentsAmount;
    private final BigDecimal salaryColumnAmount;
    private final Instant createdAt;
    private SalarySnapshotStatus status;
    private Instant paidAt;

    public MonthlySalarySnapshot(
        String debtorId,
        int year,
        int month,
        BigDecimal monthlyFreeAmount,
        BigDecimal halfFreeAmount,
        BigDecimal totalInstallmentsAmount,
        BigDecimal salaryColumnAmount
    ) {
        this(
            UUID.randomUUID().toString(),
            debtorId,
            year,
            month,
            monthlyFreeAmount,
            halfFreeAmount,
            totalInstallmentsAmount,
            salaryColumnAmount,
            SalarySnapshotStatus.PENDING,
            Instant.now(),
            null
        );
    }

    public MonthlySalarySnapshot(
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

    public void markPaid(Instant paymentDate) {
        this.status = SalarySnapshotStatus.PAID;
        this.paidAt = paymentDate;
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
