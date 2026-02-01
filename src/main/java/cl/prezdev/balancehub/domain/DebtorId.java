package cl.prezdev.balancehub.domain;

import java.util.UUID;

public class DebtorId {

    private final String id;

    public DebtorId() {
        this.id = UUID.randomUUID().toString();
    }

    public String value() {
        return id;
    }
}
