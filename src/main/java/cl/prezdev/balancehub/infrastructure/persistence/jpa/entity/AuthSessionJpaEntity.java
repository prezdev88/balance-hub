package cl.prezdev.balancehub.infrastructure.persistence.jpa.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "auth_sessions")
public class AuthSessionJpaEntity {

    @Id
    @Column(name = "token", nullable = false, length = 128)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AuthUserJpaEntity user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    protected AuthSessionJpaEntity() {
    }

    public AuthSessionJpaEntity(
        String token,
        AuthUserJpaEntity user,
        Instant expiresAt,
        boolean revoked,
        Instant createdAt,
        Instant lastUsedAt
    ) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
        this.createdAt = createdAt;
        this.lastUsedAt = lastUsedAt;
    }

    public String getToken() {
        return token;
    }

    public AuthUserJpaEntity getUser() {
        return user;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(Instant lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }
}
