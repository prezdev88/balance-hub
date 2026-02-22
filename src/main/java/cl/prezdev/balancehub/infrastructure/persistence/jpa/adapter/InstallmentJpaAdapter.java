package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.domain.Installment;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.InstallmentJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.InstallmentJpaRepository;

@Repository
public class InstallmentJpaAdapter implements InstallmentRepository {

    private final InstallmentJpaRepository repository;

    public InstallmentJpaAdapter(InstallmentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Installment> findById(String installmentId) {
        return repository.findById(installmentId).map(this::toDomain);
    }

    @Override
    public void save(List<Installment> installments) {
        if (installments == null || installments.isEmpty()) {
            return;
        }
        repository.saveAll(installments.stream().map(this::toEntity).toList());
    }

    @Override
    public void save(Installment installment) {
        repository.save(toEntity(installment));
    }

    @Override
    public List<Installment> findByDebtIds(List<String> debtIds) {
        if (debtIds == null || debtIds.isEmpty()) {
            return List.of();
        }
        return repository.findByDebtIdInOrderByDebtIdAscInstallmentNoAsc(debtIds)
            .stream()
            .map(this::toDomain)
            .toList();
    }

    private InstallmentJpaEntity toEntity(Installment installment) {
        return new InstallmentJpaEntity(
            installment.getId(),
            installment.getDebtId(),
            installment.getNumber(),
            installment.getDueDate(),
            installment.getPaidAt(),
            installment.getAmount()
        );
    }

    private Installment toDomain(InstallmentJpaEntity entity) {
        return new Installment(
            entity.getId(),
            entity.getDebtId(),
            entity.getInstallmentNo(),
            entity.getDueDate(),
            entity.getAmount(),
            entity.getPaidAt()
        );
    }
}
