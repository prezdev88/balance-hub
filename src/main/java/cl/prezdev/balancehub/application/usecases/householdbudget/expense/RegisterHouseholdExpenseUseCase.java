package cl.prezdev.balancehub.application.usecases.householdbudget.expense;

import java.time.Instant;

import cl.prezdev.balancehub.application.ports.in.RegisterHouseholdExpenseInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class RegisterHouseholdExpenseUseCase implements RegisterHouseholdExpenseInputPort {

    private final HouseholdBudgetRepository repository;

    public RegisterHouseholdExpenseUseCase(HouseholdBudgetRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("HouseholdBudgetRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public RegisterHouseholdExpenseResult execute(RegisterHouseholdExpenseCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        HouseholdBudgetConfig config = repository.findConfigByCategory(command.category())
            .orElseThrow(() -> new InvalidHouseholdBudgetException("Budget for category " + command.category() + " not configured"));

        Instant now = Instant.now();
        config.consume(command.amount(), now);
        repository.saveConfig(config);

        return new RegisterHouseholdExpenseResult(
            config.getCategory(),
            command.amount(),
            config.getRemainingAmount(),
            config.getUpdatedAt()
        );
    }
}
