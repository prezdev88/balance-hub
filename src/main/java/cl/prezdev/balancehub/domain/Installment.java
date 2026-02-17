package cl.prezdev.balancehub.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import cl.prezdev.balancehub.domain.exception.InvalidInstallmentException;

public class Installment {

    private final String id;
    private final String debtId;
    private final int number;
    private final LocalDate dueDate; // fecha de vencimiento
    private Instant paidAt;
    private final BigDecimal amount;

    public Installment(String debtId, int number, LocalDate dueDate, BigDecimal amount) {
        this(UUID.randomUUID().toString(), debtId, number, dueDate, amount, null);
    }

    public Installment(String id, String debtId, int number, LocalDate dueDate, BigDecimal amount, Instant paidAt) {
        validate(id, debtId, number, dueDate, amount);
        
        this.id = id;
        this.debtId = debtId;
        this.number = number;
        this.dueDate = dueDate;
        this.amount = amount;
        this.paidAt = paidAt;
    }

    private static void validate(String id, String debtId, int number, LocalDate dueDate, BigDecimal amount) {
        if (id == null || id.isBlank()) {
            throw new InvalidInstallmentException("Installment ID cannot be null or blank");
        }

        if (debtId == null || debtId.isBlank()) {
            throw new InvalidInstallmentException("Debt ID cannot be null or blank");
        }

        if (number <= 0) {
            throw new InvalidInstallmentException("Installment number must be greater than zero");
        }

        if (dueDate == null) {
            throw new InvalidInstallmentException("Due date cannot be null");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidInstallmentException("Amount must be greater than zero");
        }
    }

    public String getId() {
        return id;
    }

    public String getDebtId() {
        return debtId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getNumber() {
        return number;
    }

    public boolean isPaid() {
        return paidAt != null;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void pay(Instant paidAt) {
        if (paidAt == null) {
            throw new InvalidInstallmentException("Payment date cannot be null");
        }

        if (isPaid()) {
            throw new InvalidInstallmentException("Installment is already paid");
        }

        this.paidAt = paidAt;
    }
}
