package cl.prezdev.balancehub.application.usecases.salarysnapshot.get;

import cl.prezdev.balancehub.application.exception.MonthlySalarySnapshotNotFoundException;
import cl.prezdev.balancehub.application.ports.in.GetMonthlySalarySnapshotInputPort;
import cl.prezdev.balancehub.application.ports.out.MonthlySalarySnapshotRepository;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.SalarySnapshotItem;

public class GetMonthlySalarySnapshotUseCase implements GetMonthlySalarySnapshotInputPort {

    private final MonthlySalarySnapshotRepository repository;

    public GetMonthlySalarySnapshotUseCase(MonthlySalarySnapshotRepository repository) {
        this.repository = repository;
    }

    @Override
    public GetMonthlySalarySnapshotResult execute(GetMonthlySalarySnapshotCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var snapshot = repository.findByDebtorIdAndYearAndMonth(command.debtorId(), command.year(), command.month())
            .orElseThrow(() -> new MonthlySalarySnapshotNotFoundException(
                "Salary snapshot not found for debtor " + command.debtorId() + " and period " + command.month() + "/" + command.year()
            ));

        return new GetMonthlySalarySnapshotResult(toItem(snapshot));
    }

    private SalarySnapshotItem toItem(cl.prezdev.balancehub.domain.MonthlySalarySnapshot snapshot) {
        return new SalarySnapshotItem(
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
}
