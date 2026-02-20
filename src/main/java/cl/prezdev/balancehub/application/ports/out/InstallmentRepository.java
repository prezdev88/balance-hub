package cl.prezdev.balancehub.application.ports.out;

import java.util.List;
import java.util.Optional;

import cl.prezdev.balancehub.domain.Installment;

public interface InstallmentRepository {
    Optional<Installment> findById(String installmentId);

    void save(List<Installment> installments);

    void save(Installment installment);

    List<Installment> findByDebtIds(List<String> debtIds);
}
