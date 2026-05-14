package cl.prezdev.balancehub.application.usecases.householdbag.deactivate;

import cl.prezdev.balancehub.application.ports.in.DeactivateHouseholdBagInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;

public class DeactivateHouseholdBagUseCase implements DeactivateHouseholdBagInputPort {

    private final HouseholdBudgetRepository repository;

    public DeactivateHouseholdBagUseCase(HouseholdBudgetRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("householdBudgetRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public DeactivateHouseholdBagResult execute(DeactivateHouseholdBagCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }
        if (command.bagId() == null || command.bagId().isBlank()) {
            throw new IllegalArgumentException("Bag ID cannot be null or blank");
        }

        repository.deactivateBag(command.bagId());
        return new DeactivateHouseholdBagResult(command.bagId());
    }
}
