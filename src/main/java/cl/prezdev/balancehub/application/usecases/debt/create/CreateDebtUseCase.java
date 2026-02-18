package cl.prezdev.balancehub.application.usecases.debt.create;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.application.usecases.debt.create.command.CreateDebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.DebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.InstallmentCommand;
import cl.prezdev.balancehub.domain.Debt;
import cl.prezdev.balancehub.domain.Installment;

public class CreateDebtUseCase {

    private final DebtRepository debtRepository;
    private final InstallmentRepository installmentRepository;

    public CreateDebtUseCase(DebtRepository debtRepository, InstallmentRepository installmentRepository) {
        if (debtRepository == null) {
            throw new IllegalArgumentException("debtRepository must not be null");  
        }

        if (installmentRepository == null) {
            throw new IllegalArgumentException("installmentRepository must not be null");  
        }

        this.debtRepository = debtRepository;
        this.installmentRepository = installmentRepository;
    }

    public CreateDebtResult execute(CreateDebtCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");   
        }

        String debtId = createDebt(command.debt());
        createInstallments(command.installments(), debtId);

        return new CreateDebtResult(debtId);
    }

    private void createInstallments(InstallmentCommand command, String debtId) {
        List<Installment> installments = new ArrayList<>();
        
        for (int i = 0; i < command.installmentsCount(); i++) {
            LocalDate installmentDate = command.firstInstallmentDueDate().plusMonths(i);
            var installment = new Installment(debtId, i + 1, installmentDate, command.installmentAmount());

            installments.add(installment);
        }

        installmentRepository.save(installments);
    }

    private String createDebt(DebtCommand command) {
        var debt = new Debt(
            command.description(),
            command.totalAmount(),
            command.debtorId()
        );

        debtRepository.save(debt);
        
        return debt.getId();
    }

}
