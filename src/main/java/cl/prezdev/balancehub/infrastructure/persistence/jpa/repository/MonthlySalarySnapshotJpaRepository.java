package cl.prezdev.balancehub.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.MonthlySalarySnapshotJpaEntity;

public interface MonthlySalarySnapshotJpaRepository extends JpaRepository<MonthlySalarySnapshotJpaEntity, String> {
    Optional<MonthlySalarySnapshotJpaEntity> findByDebtorIdAndYearAndMonth(String debtorId, int year, int month);

    List<MonthlySalarySnapshotJpaEntity> findByYearAndMonth(int year, int month);
}
