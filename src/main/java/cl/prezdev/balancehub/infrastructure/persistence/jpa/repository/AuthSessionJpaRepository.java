package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.AuthSessionJpaEntity;

public interface AuthSessionJpaRepository extends JpaRepository<AuthSessionJpaEntity, String> {

    @Query("""
        select s
        from AuthSessionJpaEntity s
        join fetch s.user u
        where s.token = :token
          and s.revoked = false
          and s.expiresAt > :now
          and u.enabled = true
        """)
    Optional<AuthSessionJpaEntity> findActiveByToken(@Param("token") String token, @Param("now") Instant now);

    @Modifying
    @Query("update AuthSessionJpaEntity s set s.revoked = true where s.token = :token")
    int revokeByToken(@Param("token") String token);

    @Modifying
    @Query("update AuthSessionJpaEntity s set s.revoked = true where s.user.id = :userId and s.revoked = false")
    int revokeAllByUserId(@Param("userId") String userId);
}
