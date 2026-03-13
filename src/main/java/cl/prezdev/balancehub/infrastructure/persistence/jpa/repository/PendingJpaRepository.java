package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.PendingJpaEntity;

public interface PendingJpaRepository extends JpaRepository<PendingJpaEntity, String> {

    List<PendingJpaEntity> findAllByOrderByCreatedAtAscIdAsc();
}
