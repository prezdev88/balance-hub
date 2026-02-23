package cl.prezdev.balancehub.application.usecases.debt.get;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cl.prezdev.balancehub.application.exception.DebtorNotFoundException;
import cl.prezdev.balancehub.application.ports.in.GetDebtsInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.domain.Debt;
import cl.prezdev.balancehub.domain.Debtor;
import cl.prezdev.balancehub.domain.Installment;

public class GetDebtsUseCase implements GetDebtsInputPort {

    private final DebtRepository debtRepository;
    private final DebtorRepository debtorRepository;
    private final InstallmentRepository installmentRepository;

    public GetDebtsUseCase(DebtRepository debtRepository, DebtorRepository debtorRepository, InstallmentRepository installmentRepository) {
        this.debtRepository = debtRepository;
        this.debtorRepository = debtorRepository;
        this.installmentRepository = installmentRepository;
    }

    @Override
    public GetDebtsResult execute(GetDebtsCommand command) {
        Debtor debtor = debtorRepository.findById(command.debtorId())
            .orElseThrow(() -> new DebtorNotFoundException("Debtor not found with id: " + command.debtorId()));

        List<Debt> debts = debtRepository.findByDebtorIdAndDateRange(
            debtor.getId(), 
            command.startDate(), 
            command.endDate()
        );

        List<DebtItem> debtItems = mapToDebtItems(debts);

        return new GetDebtsResult(
            debtor.getId(),
            debtor.getName(),
            debtor.getEmail(),
            debtItems
        );
    }

    private List<DebtItem> mapToDebtItems(List<Debt> debts) {
        if (debts.isEmpty()) {
            return List.of();
        }

        List<String> debtIds = debts.stream()
            .map(Debt::getId)
            .toList();

        Map<String, List<Installment>> installmentsByDebtId = installmentRepository.findByDebtIds(debtIds)
            .stream()
            .collect(Collectors.groupingBy(Installment::getDebtId));

        return debts.stream()
            .map(debt -> {
                List<Installment> installments = installmentsByDebtId.getOrDefault(debt.getId(), List.of());
                List<InstallmentItem> installmentItems = mapToInstallmentItems(installments);

                return new DebtItem(
                    debt.getId(),
                    debt.getDescription(),
                    debt.getTotalAmount(),
                    debt.getCreatedAt(),
                    debt.isSettled(),
                    installmentItems
                );
            }).toList();
    }

    private List<InstallmentItem> mapToInstallmentItems(List<Installment> installments) {
        return installments.stream()
                .sorted(Comparator.comparingInt(Installment::getNumber))
                .map(installment -> new InstallmentItem(
                    installment.getId(),
                    installment.getNumber(),
                    installment.getDueDate(),
                    installment.getPaidAt(),
                    installment.getAmount()
                )).toList();
    }
}
