package cl.prezdev.balancehub.application.ports.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import cl.prezdev.balancehub.domain.Debt;

public interface DebtRepository {
    void save(Debt debt);

    Optional<Debt> findById(String debtId);

    void deleteById(String debtId);

    BigDecimal totalByDebtorId(String debtorId);

    List<Debt> findByDebtorIdAndDateRange(String debtorId, LocalDate startDate, LocalDate endDate);
}
