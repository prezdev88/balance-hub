package cl.prezdev.balancehub.infrastructure.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.AuthSessionJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.AuthUserJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.AuthSessionJpaRepository;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.AuthUserJpaRepository;

@Service
public class AuthService {

    private static final Duration SESSION_TTL = Duration.ofHours(12);

    private final AuthUserJpaRepository userRepository;
    private final AuthSessionJpaRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        AuthUserJpaRepository userRepository,
        AuthSessionJpaRepository sessionRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthLoginResult login(String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        AuthUserJpaEntity user = userRepository.findByEmailIgnoreCase(normalizedEmail)
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.isEnabled()) {
            throw new IllegalArgumentException("User access is disabled");
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = generateToken();
        Instant now = Instant.now();
        Instant expiresAt = now.plus(SESSION_TTL);

        sessionRepository.save(new AuthSessionJpaEntity(token, user, expiresAt, false, now, now));

        return new AuthLoginResult(
            token,
            "Bearer",
            expiresAt,
            user.getId(),
            user.getEmail(),
            user.getRole(),
            user.getDebtorId(),
            user.isMustChangePassword()
        );
    }

    @Transactional
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        sessionRepository.revokeByToken(token);
    }

    @Transactional
    public Optional<AuthenticatedUser> authenticate(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        Instant now = Instant.now();
        return sessionRepository.findActiveByToken(token, now).map(session -> {
            session.setLastUsedAt(now);
            AuthUserJpaEntity user = session.getUser();
            return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getDebtorId(),
                user.isMustChangePassword()
            );
        });
    }

    @Transactional
    public void changePassword(String userId, String newPassword) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User is required");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }
        if (newPassword.trim().length() < 6) {
            throw new IllegalArgumentException("New password must have at least 6 characters");
        }

        AuthUserJpaEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.isEnabled()) {
            throw new IllegalArgumentException("User access is disabled");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword.trim()));
        user.setMustChangePassword(false);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        return normalized.isBlank() ? null : normalized;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
    }
}
