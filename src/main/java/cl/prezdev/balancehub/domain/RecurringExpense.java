package cl.prezdev.balancehub.domain;

import java.math.BigDecimal;
import java.util.UUID;

import cl.prezdev.balancehub.domain.enums.ExpenseType;
import cl.prezdev.balancehub.domain.exception.InvalidFixedExpenseException;

public class RecurringExpense {

    private String id;
    private String description;
    private BigDecimal amount;
    private ExpenseType type;

    public RecurringExpense(String description, BigDecimal amount, ExpenseType type) {
         this(UUID.randomUUID().toString(), description, amount, type);
    }

    public RecurringExpense(String id, String description, BigDecimal amount, ExpenseType type) {
        validate(id, description, amount);

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
    
    private static void validate(String id, String description, BigDecimal amount) {
        if (id == null || id.isBlank()) {
            throw new InvalidFixedExpenseException("ID cannot be null or blank");
        }

        if (description == null || description.isBlank()) {
            throw new InvalidFixedExpenseException("Description cannot be null or blank");
        }

        if (amount == null) {
            throw new InvalidFixedExpenseException("Amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidFixedExpenseException("Amount must be greater than zero");
        }
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidFixedExpenseException("Amount cannot be null");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidFixedExpenseException("Amount must be greater than zero");
        }

        this.amount = amount; 
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidFixedExpenseException("Description cannot be null or blank");
        }

        this.description = description;
    }

    public ExpenseType getType() {
        return type;
    }

    public boolean isFixed() {
        return this.type == ExpenseType.FIXED;
    }

    public boolean isOptional() {
        return this.type == ExpenseType.OPTIONAL;
    }
}
