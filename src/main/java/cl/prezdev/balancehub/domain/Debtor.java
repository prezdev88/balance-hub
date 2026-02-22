package cl.prezdev.balancehub.domain;

import java.util.UUID;

import cl.prezdev.balancehub.domain.exception.InvalidDebtorException;

public class Debtor {

    private final String id;
    private final String name;
    private final String email;

    public Debtor(String name, String email) {
        this(UUID.randomUUID().toString(), name, email);
    }

    public Debtor(String id, String name, String email) {
        validate(id, name, email);

        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private static void validate(String id, String name, String email) {
        if (id == null || id.isBlank()) {
            throw new InvalidDebtorException("Debtor id cannot be null or blank");
        }

        if (name == null || name.isBlank()) {
            throw new InvalidDebtorException("Debtor name cannot be null or blank");
        }

        if (email == null || email.isBlank()) {
            throw new InvalidDebtorException("Debtor email cannot be null or blank");
        }
    }
}
