package cl.prezdev.balancehub.application.usecases.savingsgoal.create;

import cl.prezdev.balancehub.application.ports.in.CreateSavingsGoalInputPort;
import cl.prezdev.balancehub.application.ports.out.SavingsGoalRepository;
import cl.prezdev.balancehub.domain.SavingsGoal;

public class CreateSavingsGoalUseCase implements CreateSavingsGoalInputPort {

    private final SavingsGoalRepository repository;

    public CreateSavingsGoalUseCase(SavingsGoalRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("SavingsGoalRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public CreateSavingsGoalResult execute(CreateSavingsGoalCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var savingsGoal = new SavingsGoal(command.amount());
        repository.deactivateCurrentSavingsGoal();
        repository.save(savingsGoal);

        return new CreateSavingsGoalResult(
            savingsGoal.getId(),
            savingsGoal.getAmount(),
            savingsGoal.getCreatedAt()
        );
    }
}
