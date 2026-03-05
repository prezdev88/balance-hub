package cl.prezdev.balancehub.application.usecases.householdbudget.configure;

import java.time.Instant;

import cl.prezdev.balancehub.application.ports.in.ConfigureHouseholdBudgetInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;

public class ConfigureHouseholdBudgetUseCase implements ConfigureHouseholdBudgetInputPort {

    private final HouseholdBudgetRepository repository;

    public ConfigureHouseholdBudgetUseCase(HouseholdBudgetRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("HouseholdBudgetRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public ConfigureHouseholdBudgetResult execute(ConfigureHouseholdBudgetCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        Instant now = Instant.now();
        HouseholdBudgetConfig config = repository.findConfigByCategory(command.category())
            .orElseGet(() -> new HouseholdBudgetConfig(command.category(), command.monthlyAmount()));
        config.updateMonthlyAmount(command.monthlyAmount(), now);
        repository.saveConfig(config);

        return new ConfigureHouseholdBudgetResult(
            config.getCategory(),
            config.getMonthlyAmount(),
            config.getRemainingAmount(),
            config.getUpdatedAt()
        );
    }
}
