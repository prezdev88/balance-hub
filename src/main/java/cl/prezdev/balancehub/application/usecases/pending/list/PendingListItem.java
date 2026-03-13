package cl.prezdev.balancehub.application.usecases.pending.list;

import java.time.Instant;

public record PendingListItem(String id, String description, Instant createdAt) {}
