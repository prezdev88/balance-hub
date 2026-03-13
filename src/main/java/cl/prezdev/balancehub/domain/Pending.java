package cl.prezdev.balancehub.domain;

import java.time.Instant;
import java.util.UUID;

import cl.prezdev.balancehub.domain.exception.InvalidPendingException;

public class Pending {

    private final String id;
    private final String description;
    private final Instant createdAt;

    public Pending(String description) {
        this(UUID.randomUUID().toString(), description, Instant.now());
    }

    public Pending(String id, String description, Instant createdAt) {
        validate(id, description, createdAt);
        this.id = id;
        this.description = description;
        this.createdAt = createdAt;
    }

    private static void validate(String id, String description, Instant createdAt) {
        if (id == null || id.isBlank()) {
            throw new InvalidPendingException("ID cannot be null or blank");
        }

        if (description == null || description.isBlank()) {
            throw new InvalidPendingException("Description cannot be null or blank");
        }

        if (createdAt == null) {
            throw new InvalidPendingException("CreatedAt cannot be null");
        }
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
