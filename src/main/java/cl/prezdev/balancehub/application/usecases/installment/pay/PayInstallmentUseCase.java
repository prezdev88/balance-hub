package cl.prezdev.balancehub.application.usecases.installment.pay;

import cl.prezdev.balancehub.application.exception.InstallmentNotFoundException;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;

public class PayInstallmentUseCase {

    private final InstallmentRepository repository;

    public PayInstallmentUseCase(InstallmentRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }
        this.repository = repository;
    }

    public void execute(PayInstallmentCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        String installmentId = command.installmentId();

        if (installmentId == null || installmentId.isBlank()) {
            throw new IllegalArgumentException("installmentId must not be null or blank");
        }

        var installment = repository.findById(installmentId)
            .orElseThrow(() -> new InstallmentNotFoundException("Installment with id " + installmentId + " not found"));

        installment.pay(command.paymentDate());

        repository.save(installment);
    }

}
