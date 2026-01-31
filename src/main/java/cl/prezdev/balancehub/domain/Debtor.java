package cl.prezdev.balancehub.domain;

import cl.prezdev.balancehub.domain.exception.InvalidDebtorException;

public class Debtor {

    private DebtorId id;
    private String name;
    private String email;

    public Debtor(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new InvalidDebtorException("Debtor name cannot be null or bkank");
        }

        if (email == null || email.isBlank()) {
            throw new InvalidDebtorException("Debtor email cannot be null or bkank");
        }

        this.id = new DebtorId();
        this.name = name;
        this.email = email;
    }

    public DebtorId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
