package cl.prezdev.balancehub.domain;

import java.util.UUID;

public class DebtorId {

    private String id;

    public DebtorId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}
