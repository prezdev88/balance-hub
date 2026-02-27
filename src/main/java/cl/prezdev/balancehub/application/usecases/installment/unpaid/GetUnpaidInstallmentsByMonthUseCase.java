package cl.prezdev.balancehub.application.usecases.installment.unpaid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

import cl.prezdev.balancehub.application.exception.DebtorNotFoundException;
import cl.prezdev.balancehub.application.ports.in.GetUnpaidInstallmentsByMonthInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.domain.Debt;

public class GetUnpaidInstallmentsByMonthUseCase implements GetUnpaidInstallmentsByMonthInputPort {

    private static final LocalDate MIN_DATE = LocalDate.of(1970, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);

    private final DebtorRepository debtorRepository;
    private final DebtRepository debtRepository;
    private final InstallmentRepository installmentRepository;

    public GetUnpaidInstallmentsByMonthUseCase(
        DebtorRepository debtorRepository,
        DebtRepository debtRepository,
        InstallmentRepository installmentRepository
    ) {
        if (debtorRepository == null) {
            throw new IllegalArgumentException("debtorRepository cannot be null");
        }
        if (debtRepository == null) {
            throw new IllegalArgumentException("debtRepository cannot be null");
        }
        if (installmentRepository == null) {
            throw new IllegalArgumentException("installmentRepository cannot be null");
        }

        this.debtorRepository = debtorRepository;
        this.debtRepository = debtRepository;
        this.installmentRepository = installmentRepository;
    }

    @Override
    public GetUnpaidInstallmentsByMonthResult execute(GetUnpaidInstallmentsByMonthCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
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

        var debts = debtRepository.findByDebtorIdAndDateRange(command.debtorId(), MIN_DATE, MAX_DATE);
        var debtsById = debts.stream().collect(java.util.stream.Collectors.toMap(Debt::getId, Function.identity()));
        var debtIds = debts.stream().map(Debt::getId).toList();

        var allInstallments = installmentRepository.findByDebtIds(debtIds);
        Map<String, Long> installmentsPerDebt = allInstallments.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                cl.prezdev.balancehub.domain.Installment::getDebtId,
                java.util.stream.Collectors.counting()
            ));

        var items = allInstallments
            .stream()
            .filter(installment -> installment.getDueDate().getYear() == command.year())
            .filter(installment -> installment.getDueDate().getMonthValue() == command.month())
            .sorted(
                Comparator.comparing((cl.prezdev.balancehub.domain.Installment i) -> i.getDueDate())
                    .thenComparing(i -> i.getNumber())
            )
            .map(installment -> {
                Debt debt = debtsById.get(installment.getDebtId());
                String debtDescription = debt != null ? debt.getDescription() : "Sin descripción";
                return new UnpaidInstallmentItem(
                    installment.getId(),
                    installment.getDebtId(),
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

        BigDecimal total = items.stream()
            .filter(item -> !item.paid())
            .map(UnpaidInstallmentItem::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new GetUnpaidInstallmentsByMonthResult(
            debtor.getId(),
            debtor.getName(),
            debtor.getEmail(),
            command.year(),
            command.month(),
            total,
            items
        );
    }
}
