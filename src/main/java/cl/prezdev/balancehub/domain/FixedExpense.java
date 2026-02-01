package cl.prezdev.balancehub.domain;

import java.util.UUID;

import cl.prezdev.balancehub.domain.exception.InvalidFixedExpenseException;

public class FixedExpense {

    private final String id;
    private final String description;
    private final double amount;

    public FixedExpense(String description, double amount) {
        validate(description, amount);

        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    
    public double getAmount() {
        return amount;
    }
    
    private static void validate(String description, double amount) {
        if (description == null || description.isBlank()) {
            throw new InvalidFixedExpenseException("Description cannot be null or blank");
        }

        if (amount <= 0) {
            throw new InvalidFixedExpenseException("Amount must be greater than zero");
        }
    }
}
