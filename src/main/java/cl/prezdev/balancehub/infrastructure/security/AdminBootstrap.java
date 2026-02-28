package cl.prezdev.balancehub.infrastructure.security;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cl.prezdev.balancehub.domain.enums.UserRole;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.AuthUserJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.AuthUserJpaRepository;

@Component
public class AdminBootstrap implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AdminBootstrap.class);

    private final AuthUserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@balancehub.local}")
    private String adminEmail;

    @Value("${app.admin.password:Admin1234}")
    private String adminPassword;

    public AdminBootstrap(AuthUserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(UserRole.ADMIN)) {
            return;
        }

        String normalizedEmail = adminEmail == null
            ? "admin@balancehub.local"
            : adminEmail.trim().toLowerCase(Locale.ROOT);
        if (normalizedEmail.isBlank()) {
            normalizedEmail = "admin@balancehub.local";
        }

        String password = adminPassword == null || adminPassword.isBlank() ? "Admin1234" : adminPassword;

        Instant now = Instant.now();
        userRepository.save(new AuthUserJpaEntity(
            UUID.randomUUID().toString(),
            normalizedEmail,
            passwordEncoder.encode(password),
            UserRole.ADMIN,
            null,
            true,
            now,
            now
        ));

        LOG.warn("No ADMIN user found. Bootstrap admin created with email '{}'.", normalizedEmail);
        LOG.warn("Change admin password as soon as possible.");
    }
}
