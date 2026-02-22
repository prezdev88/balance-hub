package cl.prezdev.balancehub.infrastructure.persistence.jpa.entity;

import java.math.BigDecimal;

import cl.prezdev.balancehub.domain.enums.ExpenseType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recurring_expenses")
public class RecurringExpenseJpaEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ExpenseType type;

    protected RecurringExpenseJpaEntity() {
    }

    public RecurringExpenseJpaEntity(String id, String description, BigDecimal amount, ExpenseType type) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ExpenseType getType() {
        return type;
    }
}
