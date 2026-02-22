package cl.prezdev.balancehub.application.ports.out;

import java.time.LocalDate;
import java.util.List;

import cl.prezdev.balancehub.domain.Debt;

public interface DebtRepository {
    void save(Debt debt);

    List<Debt> findByDebtorIdAndDateRange(String debtorId, LocalDate startDate, LocalDate endDate);
}
