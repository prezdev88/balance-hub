package cl.prezdev.balancehub.application.usecases.debt.getdetail;

import java.util.Comparator;
import java.util.List;

import cl.prezdev.balancehub.application.exception.DebtNotFoundException;
import cl.prezdev.balancehub.application.exception.DebtorNotFoundException;
import cl.prezdev.balancehub.application.ports.in.GetDebtDetailInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.application.usecases.debt.get.DebtItem;
import cl.prezdev.balancehub.application.usecases.debt.get.InstallmentItem;

public class GetDebtDetailUseCase implements GetDebtDetailInputPort {

    private final DebtRepository debtRepository;
    private final DebtorRepository debtorRepository;
    private final InstallmentRepository installmentRepository;

    public GetDebtDetailUseCase(
        DebtRepository debtRepository,
        DebtorRepository debtorRepository,
        InstallmentRepository installmentRepository
    ) {
        this.debtRepository = debtRepository;
        this.debtorRepository = debtorRepository;
        this.installmentRepository = installmentRepository;
    }

    @Override
    public GetDebtDetailResult execute(GetDebtDetailCommand command) {
        var debt = debtRepository.findById(command.debtId()).orElseThrow(
            () -> new DebtNotFoundException("Debt with id " + command.debtId() + " not found")
        );

        var debtor = debtorRepository.findById(debt.getDebtorId()).orElseThrow(
            () -> new DebtorNotFoundException("Debtor with id " + debt.getDebtorId() + " not found")
        );

        var installments = installmentRepository.findByDebtIds(List.of(debt.getId()))
            .stream()
            .sorted(Comparator.comparingInt(cl.prezdev.balancehub.domain.Installment::getNumber))
            .map(installment -> new InstallmentItem(
                installment.getId(),
                installment.getNumber(),
                installment.getDueDate(),
                installment.getPaidAt(),
                installment.getAmount()
            ))
            .toList();

        boolean settled = !installments.isEmpty() && installments.stream().allMatch(item -> item.paidAt() != null);

        var debtItem = new DebtItem(
            debt.getId(),
            debt.getDescription(),
            debt.getTotalAmount(),
            debt.getCreatedAt(),
            settled,
            installments
        );

        return new GetDebtDetailResult(
            debtor.getId(),
            debtor.getName(),
            debtor.getEmail(),
            debtItem
        );
    }
}
