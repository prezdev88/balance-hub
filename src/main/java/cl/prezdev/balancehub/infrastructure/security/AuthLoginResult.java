package cl.prezdev.balancehub.infrastructure.security;

import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.UserRole;

public record AuthLoginResult(
    String accessToken,
    String tokenType,
    Instant expiresAt,
    String userId,
    String email,
    UserRole role,
    String debtorId,
    boolean mustChangePassword
) {}
