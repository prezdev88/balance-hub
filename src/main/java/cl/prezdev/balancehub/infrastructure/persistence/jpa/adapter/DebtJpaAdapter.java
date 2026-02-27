package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.domain.Debt;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.DebtJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.DebtJpaRepository;

@Repository
public class DebtJpaAdapter implements DebtRepository {

    private final DebtJpaRepository repository;

    public DebtJpaAdapter(DebtJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Debt debt) {
        repository.save(toEntity(debt));
    }

    @Override
    public Optional<Debt> findById(String debtId) {
        return repository.findById(debtId).map(this::toDomain);
    }

    @Override
    public void deleteById(String debtId) {
        repository.deleteById(debtId);
    }

    @Override
    public BigDecimal totalByDebtorId(String debtorId) {
        BigDecimal total = repository.sumPendingAmountByDebtorId(debtorId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<Debt> findByDebtorIdAndDateRange(String debtorId, LocalDate startDate, LocalDate endDate) {
        var startInclusive = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        var endExclusive = endDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return repository
            .findByDebtorIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDescIdAsc(
                debtorId,
                startInclusive,
                endExclusive
            )
            .stream()
            .map(this::toDomain)
            .toList();
    }

    private DebtJpaEntity toEntity(Debt debt) {
        return new DebtJpaEntity(
            debt.getId(),
            debt.getDescription(),
            debt.getTotalAmount(),
            debt.getDebtorId(),
            debt.getCreatedAt(),
            debt.isSettled()
        );
    }

    private Debt toDomain(DebtJpaEntity entity) {
        return new Debt(
            entity.getId(),
            entity.getDescription(),
            entity.getTotalAmount(),
            entity.getDebtorId(),
            entity.getCreatedAt(),
            entity.isSettled()
        );
    }
}
