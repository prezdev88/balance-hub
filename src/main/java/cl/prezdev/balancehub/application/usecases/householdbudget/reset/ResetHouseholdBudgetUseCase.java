package cl.prezdev.balancehub.application.usecases.householdbudget.reset;

import java.time.Instant;

import cl.prezdev.balancehub.application.ports.in.ResetHouseholdBudgetInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class ResetHouseholdBudgetUseCase implements ResetHouseholdBudgetInputPort {

    private final HouseholdBudgetRepository repository;

    public ResetHouseholdBudgetUseCase(HouseholdBudgetRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("HouseholdBudgetRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public ResetHouseholdBudgetResult execute(ResetHouseholdBudgetCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var config = repository.findConfigByCategory(command.category())
            .orElseThrow(() -> new InvalidHouseholdBudgetException("Budget for category " + command.category() + " not configured"));

        config.reset(Instant.now());
        repository.saveConfig(config);

        return new ResetHouseholdBudgetResult(
            config.getCategory(),
            config.getMonthlyAmount(),
            config.getRemainingAmount(),
            config.getUpdatedAt()
        );
    }
}
