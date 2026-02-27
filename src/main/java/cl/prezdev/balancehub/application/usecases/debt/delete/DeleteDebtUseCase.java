package cl.prezdev.balancehub.application.usecases.debt.delete;

import cl.prezdev.balancehub.application.exception.DebtNotFoundException;
import cl.prezdev.balancehub.application.ports.in.DeleteDebtInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;

public class DeleteDebtUseCase implements DeleteDebtInputPort {

    private final DebtRepository debtRepository;

    public DeleteDebtUseCase(DebtRepository debtRepository) {
        if (debtRepository == null) {
            throw new IllegalArgumentException("debtRepository cannot be null");
        }
        this.debtRepository = debtRepository;
    }

    @Override
    public void execute(DeleteDebtCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        debtRepository.findById(command.debtId()).orElseThrow(
            () -> new DebtNotFoundException("Debt with id " + command.debtId() + " not found")
        );

        debtRepository.deleteById(command.debtId());
    }
}
