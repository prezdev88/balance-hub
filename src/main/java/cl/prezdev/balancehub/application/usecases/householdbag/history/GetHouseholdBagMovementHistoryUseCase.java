package cl.prezdev.balancehub.application.usecases.householdbag.history;

import java.util.List;

import cl.prezdev.balancehub.application.ports.in.GetHouseholdBagMovementHistoryInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBagMovementRepository;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.domain.HouseholdBagMovement;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class GetHouseholdBagMovementHistoryUseCase implements GetHouseholdBagMovementHistoryInputPort {

    private final HouseholdBudgetRepository budgetRepository;
    private final HouseholdBagMovementRepository movementRepository;

    public GetHouseholdBagMovementHistoryUseCase(
        HouseholdBudgetRepository budgetRepository,
        HouseholdBagMovementRepository movementRepository
    ) {
        if (budgetRepository == null) {
            throw new IllegalArgumentException("householdBudgetRepository cannot be null");
        }
        if (movementRepository == null) {
            throw new IllegalArgumentException("householdBagMovementRepository cannot be null");
        }
        this.budgetRepository = budgetRepository;
        this.movementRepository = movementRepository;
    }

    @Override
    public GetHouseholdBagMovementHistoryResult execute(GetHouseholdBagMovementHistoryCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        String bagId = requireBag(command.bagId());
        List<HouseholdBagMovementHistoryItem> items = movementRepository.findByBagId(bagId).stream()
            .map(this::toItem)
            .toList();
        return new GetHouseholdBagMovementHistoryResult(bagId, items);
    }

    private String requireBag(String bagId) {
        if (bagId == null || bagId.isBlank()) {
            throw new InvalidHouseholdBudgetException("Bag ID cannot be null or blank");
        }
        return budgetRepository.findById(bagId)
            .map(bag -> bag.getId())
            .orElseThrow(() -> new InvalidHouseholdBudgetException("Bag " + bagId + " not found"));
    }

    private HouseholdBagMovementHistoryItem toItem(HouseholdBagMovement movement) {
        return new HouseholdBagMovementHistoryItem(
            movement.getId(),
            movement.getAmount(),
            movement.getMovementType(),
            movement.getCreatedAt()
        );
    }
}
