package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.MonthlySalarySnapshotRepository;
import cl.prezdev.balancehub.domain.MonthlySalarySnapshot;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.MonthlySalarySnapshotJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.MonthlySalarySnapshotJpaRepository;

@Repository
public class MonthlySalarySnapshotJpaAdapter implements MonthlySalarySnapshotRepository {

    private final MonthlySalarySnapshotJpaRepository repository;

    public MonthlySalarySnapshotJpaAdapter(MonthlySalarySnapshotJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<MonthlySalarySnapshot> findByDebtorIdAndYearAndMonth(String debtorId, int year, int month) {
        return repository.findByDebtorIdAndYearAndMonth(debtorId, year, month).map(this::toDomain);
    }

    @Override
    public List<MonthlySalarySnapshot> findByYearAndMonth(int year, int month) {
        return repository.findByYearAndMonth(year, month).stream().map(this::toDomain).toList();
    }

    @Override
    public void save(MonthlySalarySnapshot snapshot) {
        repository.save(toEntity(snapshot));
    }

    private MonthlySalarySnapshotJpaEntity toEntity(MonthlySalarySnapshot snapshot) {
        return new MonthlySalarySnapshotJpaEntity(
            snapshot.getId(),
            snapshot.getDebtorId(),
            snapshot.getYear(),
            snapshot.getMonth(),
            snapshot.getMonthlyFreeAmount(),
            snapshot.getHalfFreeAmount(),
            snapshot.getTotalInstallmentsAmount(),
            snapshot.getSalaryColumnAmount(),
            snapshot.getStatus(),
            snapshot.getCreatedAt(),
            snapshot.getPaidAt()
        );
    }

    private MonthlySalarySnapshot toDomain(MonthlySalarySnapshotJpaEntity entity) {
        return new MonthlySalarySnapshot(
            entity.getId(),
            entity.getDebtorId(),
            entity.getYear(),
            entity.getMonth(),
            entity.getMonthlyFreeAmount(),
            entity.getHalfFreeAmount(),
            entity.getTotalInstallmentsAmount(),
            entity.getSalaryColumnAmount(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getPaidAt()
        );
    }
}
