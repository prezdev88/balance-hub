package cl.prezdev.balancehub.application.ports.out;

import java.util.List;

import cl.prezdev.balancehub.domain.Installment;

public interface InstallmentRepository {
    void save(List<Installment> installments);

    List<Installment> findByDebtId(String debtId);
}
