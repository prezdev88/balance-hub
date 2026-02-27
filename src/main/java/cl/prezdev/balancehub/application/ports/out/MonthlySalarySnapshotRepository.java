package cl.prezdev.balancehub.application.ports.out;

import java.util.List;
import java.util.Optional;

import cl.prezdev.balancehub.domain.MonthlySalarySnapshot;

public interface MonthlySalarySnapshotRepository {
    Optional<MonthlySalarySnapshot> findByDebtorIdAndYearAndMonth(String debtorId, int year, int month);

    List<MonthlySalarySnapshot> findByYearAndMonth(int year, int month);

    void save(MonthlySalarySnapshot snapshot);
}
