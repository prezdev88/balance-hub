package cl.prezdev.balancehub.application.usecases.salarysnapshot.pay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;

import cl.prezdev.balancehub.application.exception.DebtorNotFoundException;
import cl.prezdev.balancehub.application.ports.in.PayMonthlySalaryInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.application.ports.out.MonthlySalarySnapshotRepository;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.application.ports.out.SalaryRepository;
import cl.prezdev.balancehub.application.ports.out.SavingsGoalRepository;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.SalarySnapshotItem;
import cl.prezdev.balancehub.domain.MonthlySalarySnapshot;
import cl.prezdev.balancehub.domain.enums.ExpenseType;
import cl.prezdev.balancehub.domain.enums.SalarySnapshotStatus;

public class PayMonthlySalaryUseCase implements PayMonthlySalaryInputPort {

    private static final LocalDate MIN_DATE = LocalDate.of(1970, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);

    private final DebtorRepository debtorRepository;
    private final DebtRepository debtRepository;
    private final InstallmentRepository installmentRepository;
    private final SalaryRepository salaryRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final MonthlySalarySnapshotRepository snapshotRepository;

    public PayMonthlySalaryUseCase(
        DebtorRepository debtorRepository,
        DebtRepository debtRepository,
        InstallmentRepository installmentRepository,
        SalaryRepository salaryRepository,
        SavingsGoalRepository savingsGoalRepository,
        RecurringExpenseRepository recurringExpenseRepository,
        MonthlySalarySnapshotRepository snapshotRepository
    ) {
        this.debtorRepository = debtorRepository;
        this.debtRepository = debtRepository;
        this.installmentRepository = installmentRepository;
        this.salaryRepository = salaryRepository;
        this.savingsGoalRepository = savingsGoalRepository;
        this.recurringExpenseRepository = recurringExpenseRepository;
        this.snapshotRepository = snapshotRepository;
    }

    @Override
    public PayMonthlySalaryResult execute(PayMonthlySalaryCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }
        if (command.month() < 1 || command.month() > 12) {
            throw new IllegalArgumentException("month must be between 1 and 12");
        }
        if (command.year() < 1970 || command.year() > 9999) {
            throw new IllegalArgumentException("year must be between 1970 and 9999");
        }

        debtorRepository.findById(command.debtorId()).orElseThrow(
            () -> new DebtorNotFoundException("Debtor with id " + command.debtorId() + " not found")
        );

        Instant paymentDate = command.paymentDate() != null ? command.paymentDate() : Instant.now();
        var existing = snapshotRepository.findByDebtorIdAndYearAndMonth(command.debtorId(), command.year(), command.month());
        if (existing.isPresent()) {
            var snapshot = existing.get();
            if (snapshot.getStatus() != SalarySnapshotStatus.PAID) {
                snapshot.markPaid(paymentDate);
                snapshotRepository.save(snapshot);
            }
            return new PayMonthlySalaryResult(toItem(snapshot), false);
        }

        var monthlyFreeAmount = calculateMonthlyFreeAmount();
        var halfFreeAmount = monthlyFreeAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        var totalInstallmentsAmount = calculateMonthlyInstallmentsAmount(command.debtorId(), command.year(), command.month());
        var salaryColumnAmount = halfFreeAmount.subtract(totalInstallmentsAmount);

        var snapshot = new MonthlySalarySnapshot(
            command.debtorId(),
            command.year(),
            command.month(),
            monthlyFreeAmount,
            halfFreeAmount,
            totalInstallmentsAmount,
            salaryColumnAmount
        );
        snapshot.markPaid(paymentDate);
        snapshotRepository.save(snapshot);

        return new PayMonthlySalaryResult(toItem(snapshot), true);
    }

    private BigDecimal calculateMonthlyFreeAmount() {
        var currentSalary = salaryRepository.findActive().map(cl.prezdev.balancehub.domain.Salary::getAmount).orElse(BigDecimal.ZERO);
        var monthlySavingsGoal = savingsGoalRepository.findActive()
            .map(cl.prezdev.balancehub.domain.SavingsGoal::getAmount)
            .orElse(BigDecimal.ZERO);
        var fixedExpenses = recurringExpenseRepository.totalByType(ExpenseType.FIXED);
        var optionalExpenses = recurringExpenseRepository.totalByType(ExpenseType.OPTIONAL);
        return currentSalary.subtract(monthlySavingsGoal).subtract(fixedExpenses).subtract(optionalExpenses);
    }

    private BigDecimal calculateMonthlyInstallmentsAmount(String debtorId, int year, int month) {
        var debts = debtRepository.findByDebtorIdAndDateRange(debtorId, MIN_DATE, MAX_DATE);
        if (debts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        var debtIds = debts.stream().map(cl.prezdev.balancehub.domain.Debt::getId).toList();

        return installmentRepository.findByDebtIds(debtIds)
            .stream()
            .filter(installment -> installment.getDueDate().getYear() == year)
            .filter(installment -> installment.getDueDate().getMonthValue() == month)
            .map(cl.prezdev.balancehub.domain.Installment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private SalarySnapshotItem toItem(MonthlySalarySnapshot snapshot) {
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
