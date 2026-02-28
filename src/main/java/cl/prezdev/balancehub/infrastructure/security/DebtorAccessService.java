package cl.prezdev.balancehub.infrastructure.security;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.prezdev.balancehub.domain.enums.UserRole;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.AuthUserJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.AuthSessionJpaRepository;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.AuthUserJpaRepository;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.DebtorJpaRepository;

@Service
public class DebtorAccessService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final DebtorJpaRepository debtorRepository;
    private final AuthUserJpaRepository userRepository;
    private final AuthSessionJpaRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    public DebtorAccessService(
        DebtorJpaRepository debtorRepository,
        AuthUserJpaRepository userRepository,
        AuthSessionJpaRepository sessionRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.debtorRepository = debtorRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public DebtorAccessResult grant(String debtorId, String requestedPassword) {
        var debtor = debtorRepository.findById(debtorId)
            .orElseThrow(() -> new IllegalArgumentException("Debtor with id " + debtorId + " not found"));

        String email = normalizeEmail(debtor.getEmail());
        if (email == null) {
            throw new IllegalArgumentException("Debtor email is required to grant access");
        }

        PasswordData passwordData = resolvePassword(requestedPassword);
        String encodedPassword = passwordEncoder.encode(passwordData.password());
        Instant now = Instant.now();

        AuthUserJpaEntity user = userRepository.findByDebtorId(debtorId).orElseGet(() ->
            new AuthUserJpaEntity(
                UUID.randomUUID().toString(),
                email,
                encodedPassword,
                UserRole.DEBTOR,
                debtorId,
                true,
                now,
                now
            )
        );

        user.setEmail(email);
        user.setRole(UserRole.DEBTOR);
        user.setDebtorId(debtorId);
        user.setEnabled(true);
        user.setPasswordHash(encodedPassword);
        user.setUpdatedAt(now);

        userRepository.save(user);
        sessionRepository.revokeAllByUserId(user.getId());

        return new DebtorAccessResult(debtorId, email, true, passwordData.password(), passwordData.generated());
    }

    @Transactional
    public DebtorAccessResult resetPassword(String debtorId, String requestedPassword) {
        AuthUserJpaEntity user = userRepository.findByDebtorId(debtorId)
            .orElseThrow(() -> new IllegalArgumentException("Debtor access does not exist for id " + debtorId));

        PasswordData passwordData = resolvePassword(requestedPassword);
        user.setPasswordHash(passwordEncoder.encode(passwordData.password()));
        user.setEnabled(true);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        sessionRepository.revokeAllByUserId(user.getId());

        return new DebtorAccessResult(debtorId, user.getEmail(), true, passwordData.password(), passwordData.generated());
    }

    @Transactional
    public DebtorAccessResult revoke(String debtorId) {
        AuthUserJpaEntity user = userRepository.findByDebtorId(debtorId)
            .orElseThrow(() -> new IllegalArgumentException("Debtor access does not exist for id " + debtorId));

        user.setEnabled(false);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        sessionRepository.revokeAllByUserId(user.getId());

        return new DebtorAccessResult(debtorId, user.getEmail(), false, null, false);
    }

    private PasswordData resolvePassword(String requestedPassword) {
        if (requestedPassword != null && !requestedPassword.isBlank()) {
            return new PasswordData(requestedPassword.trim(), false);
        }

        String generated = "Balance" + (1000 + RANDOM.nextInt(9000));
        return new PasswordData(generated, true);
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        return normalized.isBlank() ? null : normalized;
    }

    private record PasswordData(String password, boolean generated) {
    }
}
