package cl.prezdev.balancehub.infrastructure.persistence.jpa.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "installments")
public class InstallmentJpaEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "debt_id", nullable = false, length = 64)
    private String debtId;

    @Column(name = "installment_no", nullable = false)
    private int installmentNo;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    protected InstallmentJpaEntity() {
    }

    public InstallmentJpaEntity(String id, String debtId, int installmentNo, LocalDate dueDate, Instant paidAt, BigDecimal amount) {
        this.id = id;
        this.debtId = debtId;
        this.installmentNo = installmentNo;
        this.dueDate = dueDate;
        this.paidAt = paidAt;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getDebtId() {
        return debtId;
    }

    public int getInstallmentNo() {
        return installmentNo;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
