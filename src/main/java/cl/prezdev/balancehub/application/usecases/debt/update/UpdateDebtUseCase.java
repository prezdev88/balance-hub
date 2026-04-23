package cl.prezdev.balancehub.application.usecases.debt.update;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import cl.prezdev.balancehub.application.exception.DebtNotFoundException;
import cl.prezdev.balancehub.application.ports.in.UpdateDebtInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.application.usecases.debt.update.command.UpdateDebtCommand;
import cl.prezdev.balancehub.domain.Debt;
import cl.prezdev.balancehub.domain.Installment;

public class UpdateDebtUseCase implements UpdateDebtInputPort {

    private final DebtRepository debtRepository;
    private final InstallmentRepository installmentRepository;

    public UpdateDebtUseCase(DebtRepository debtRepository, InstallmentRepository installmentRepository) {
        if (debtRepository == null) {
            throw new IllegalArgumentException("debtRepository must not be null");
        }
        if (installmentRepository == null) {
            throw new IllegalArgumentException("installmentRepository must not be null");
        }

        this.debtRepository = debtRepository;
        this.installmentRepository = installmentRepository;
    }

    @Override
    public UpdateDebtResult execute(UpdateDebtCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        Debt existingDebt = debtRepository.findById(command.debtId())
            .orElseThrow(() -> new DebtNotFoundException(command.debtId()));

        List<Installment> existingInstallments = installmentRepository.findByDebtIds(List.of(command.debtId()));

        Debt updatedDebt = existingDebt
            .withDescription(command.description())
            .withTotalAmount(command.totalAmount())
            .withCreatedAt(command.createdAt().atStartOfDay(ZoneOffset.UTC).toInstant());

        boolean allPaid = existingInstallments.stream()
            .allMatch(Installment::isPaid);

        updatedDebt = updatedDebt.withSettled(allPaid);

        debtRepository.update(updatedDebt);

        List<Installment> updatedInstallments = new ArrayList<>();
        for (int i = 0; i < existingInstallments.size(); i++) {
            Installment oldInstallment = existingInstallments.get(i);
            var newAmount = command.installmentAmount();
            updatedInstallments.add(oldInstallment.withAmount(newAmount));
        }

        installmentRepository.save(updatedInstallments);

        return new UpdateDebtResult(command.debtId());
    }
}