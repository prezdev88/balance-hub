package cl.prezdev.balancehub.application.usecases.report.monthly;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

import cl.prezdev.balancehub.application.exception.DebtorNotFoundException;
import cl.prezdev.balancehub.application.ports.in.GetMonthlySummaryReportInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.application.ports.out.MonthlySalarySnapshotRepository;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.application.ports.out.SalaryRepository;
import cl.prezdev.balancehub.application.ports.out.SavingsGoalRepository;
import cl.prezdev.balancehub.domain.Debt;
import cl.prezdev.balancehub.domain.enums.ExpenseType;

public class GetMonthlySummaryReportUseCase implements GetMonthlySummaryReportInputPort {

    private static final LocalDate MIN_DATE = LocalDate.of(1970, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);

    private final DebtorRepository debtorRepository;
    private final DebtRepository debtRepository;
    private final InstallmentRepository installmentRepository;
    private final MonthlySalarySnapshotRepository snapshotRepository;
    private final SalaryRepository salaryRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;

    public GetMonthlySummaryReportUseCase(
        DebtorRepository debtorRepository,
        DebtRepository debtRepository,
        InstallmentRepository installmentRepository,
        MonthlySalarySnapshotRepository snapshotRepository,
        SalaryRepository salaryRepository,
        SavingsGoalRepository savingsGoalRepository,
        RecurringExpenseRepository recurringExpenseRepository
    ) {
        this.debtorRepository = debtorRepository;
        this.debtRepository = debtRepository;
        this.installmentRepository = installmentRepository;
        this.snapshotRepository = snapshotRepository;
        this.salaryRepository = salaryRepository;
        this.savingsGoalRepository = savingsGoalRepository;
        this.recurringExpenseRepository = recurringExpenseRepository;
    }

    @Override
    public GetMonthlySummaryReportResult execute(GetMonthlySummaryReportCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }
        if (command.month() < 1 || command.month() > 12) {
            throw new IllegalArgumentException("month must be between 1 and 12");
        }
        if (command.year() < 1970 || command.year() > 9999) {
            throw new IllegalArgumentException("year must be between 1970 and 9999");
        }

        var debtor = debtorRepository.findById(command.debtorId()).orElseThrow(
            () -> new DebtorNotFoundException("Debtor with id " + command.debtorId() + " not found")
        );
        var snapshot = snapshotRepository.findByDebtorIdAndYearAndMonth(command.debtorId(), command.year(), command.month());

        var debts = debtRepository.findByDebtorIdAndDateRange(command.debtorId(), MIN_DATE, MAX_DATE);
        var debtsById = debts.stream().collect(java.util.stream.Collectors.toMap(Debt::getId, Function.identity()));
        var debtIds = debts.stream().map(Debt::getId).toList();

        Map<String, Long> installmentsPerDebt = installmentRepository.findByDebtIds(debtIds)
            .stream()
            .collect(java.util.stream.Collectors.groupingBy(
                cl.prezdev.balancehub.domain.Installment::getDebtId,
                java.util.stream.Collectors.counting()
            ));

        var monthlyInstallments = installmentRepository.findByDebtIds(debtIds)
            .stream()
            .filter(installment -> installment.getDueDate().getYear() == command.year())
            .filter(installment -> installment.getDueDate().getMonthValue() == command.month())
            .sorted(Comparator.comparing(cl.prezdev.balancehub.domain.Installment::getDueDate).thenComparing(cl.prezdev.balancehub.domain.Installment::getNumber))
            .map(installment -> {
                var debt = debtsById.get(installment.getDebtId());
                var debtDescription = debt != null ? debt.getDescription() : "Sin descripción";
                return new MonthlySummaryInstallmentItem(
                    installment.getId(),
                    debtDescription,
                    installment.getNumber(),
                    installmentsPerDebt.getOrDefault(installment.getDebtId(), 0L).intValue(),
                    installment.getDueDate(),
                    installment.getAmount(),
                    installment.isPaid(),
                    installment.getPaidAt()
                );
            })
            .toList();

        var previewMonthlyFreeAmount = salaryRepository.findActive()
            .map(salary -> salary.getAmount())
            .orElse(java.math.BigDecimal.ZERO)
            .subtract(
                savingsGoalRepository.findActive()
                    .map(savingsGoal -> savingsGoal.getAmount())
                    .orElse(java.math.BigDecimal.ZERO)
            )
            .subtract(recurringExpenseRepository.totalByType(ExpenseType.FIXED))
            .subtract(recurringExpenseRepository.totalByType(ExpenseType.OPTIONAL));
        var previewHalfFreeAmount = previewMonthlyFreeAmount.divide(java.math.BigDecimal.valueOf(2), java.math.RoundingMode.HALF_UP);
        var previewTotalInstallmentsAmount = monthlyInstallments.stream()
            .map(MonthlySummaryInstallmentItem::amount)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        var previewSalaryColumnAmount = previewHalfFreeAmount.subtract(previewTotalInstallmentsAmount);

        return new GetMonthlySummaryReportResult(
            debtor.getId(),
            debtor.getName(),
            debtor.getEmail(),
            command.year(),
            command.month(),
            snapshot.map(cl.prezdev.balancehub.domain.MonthlySalarySnapshot::getMonthlyFreeAmount).orElse(previewMonthlyFreeAmount),
            snapshot.map(cl.prezdev.balancehub.domain.MonthlySalarySnapshot::getHalfFreeAmount).orElse(previewHalfFreeAmount),
            snapshot.map(cl.prezdev.balancehub.domain.MonthlySalarySnapshot::getTotalInstallmentsAmount).orElse(previewTotalInstallmentsAmount),
            snapshot.map(cl.prezdev.balancehub.domain.MonthlySalarySnapshot::getSalaryColumnAmount).orElse(previewSalaryColumnAmount),
            snapshot.map(cl.prezdev.balancehub.domain.MonthlySalarySnapshot::getStatus).orElse(null),
            snapshot.map(cl.prezdev.balancehub.domain.MonthlySalarySnapshot::getPaidAt).orElse(null),
            monthlyInstallments
        );
    }
}
