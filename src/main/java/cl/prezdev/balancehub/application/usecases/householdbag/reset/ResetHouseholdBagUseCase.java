package cl.prezdev.balancehub.application.usecases.householdbag.reset;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.application.ports.in.ResetHouseholdBagInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBagMovementRepository;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.domain.HouseholdBagMovement;
import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;
import cl.prezdev.balancehub.domain.enums.HouseholdBagMovementType;
import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class ResetHouseholdBagUseCase implements ResetHouseholdBagInputPort {

    private final HouseholdBudgetRepository budgetRepository;
    private final HouseholdBagMovementRepository movementRepository;

    public ResetHouseholdBagUseCase(
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
    public ResetHouseholdBagResult execute(ResetHouseholdBagCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        HouseholdBudgetConfig bag = resolveBag(command.bagReference());
        Instant now = Instant.now();
        bag.reset(now);
        budgetRepository.save(bag);

        BigDecimal resetAmount = bag.getMonthlyAmount();
        HouseholdBagMovement movement = new HouseholdBagMovement(
            bag.getId(),
            resetAmount,
            HouseholdBagMovementType.RESET
        );
        movementRepository.save(movement);

        return new ResetHouseholdBagResult(
            bag.getId(),
            bag.getMonthlyAmount(),
            bag.getRemainingAmount(),
            bag.getUpdatedAt()
        );
    }

    private HouseholdBudgetConfig resolveBag(String bagReference) {
        if (bagReference == null || bagReference.isBlank()) {
            throw new InvalidHouseholdBudgetException("Bag reference cannot be null or blank");
        }

        HouseholdBudgetConfig bagById = budgetRepository.findById(bagReference).orElse(null);
        if (bagById != null) {
            return bagById;
        }

        HouseholdBudgetCategory category = resolveCategory(bagReference);
        return budgetRepository.findConfigByCategory(category)
            .orElseThrow(() -> new InvalidHouseholdBudgetException("Bag " + bagReference + " not found"));
    }

    private HouseholdBudgetCategory resolveCategory(String bagReference) {
        try {
            return HouseholdBudgetCategory.valueOf(bagReference);
        } catch (IllegalArgumentException ex) {
            throw new InvalidHouseholdBudgetException("Bag " + bagReference + " not found");
        }
    }
}
