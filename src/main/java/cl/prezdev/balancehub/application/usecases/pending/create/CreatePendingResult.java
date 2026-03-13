package cl.prezdev.balancehub.application.usecases.pending.create;

import java.time.Instant;

public record CreatePendingResult(String pendingId, String description, Instant createdAt) {}
