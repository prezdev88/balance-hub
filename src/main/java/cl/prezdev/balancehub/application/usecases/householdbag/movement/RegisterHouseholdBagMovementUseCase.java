package cl.prezdev.balancehub.application.usecases.householdbag.movement;

import java.time.Instant;

import cl.prezdev.balancehub.application.ports.in.RegisterHouseholdBagMovementInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBagMovementRepository;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.domain.HouseholdBagMovement;
import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;
import cl.prezdev.balancehub.domain.enums.HouseholdBagMovementType;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class RegisterHouseholdBagMovementUseCase implements RegisterHouseholdBagMovementInputPort {

    private final HouseholdBudgetRepository budgetRepository;
    private final HouseholdBagMovementRepository movementRepository;

    public RegisterHouseholdBagMovementUseCase(
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
    public RegisterHouseholdBagMovementResult execute(RegisterHouseholdBagMovementCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        HouseholdBudgetConfig bag = budgetRepository.findById(command.bagId())
            .orElseThrow(() -> new InvalidHouseholdBudgetException("Bag " + command.bagId() + " not found"));

        Instant now = Instant.now();
        bag.consume(command.amount(), now);
        budgetRepository.save(bag);

        HouseholdBagMovementType movementType = resolveMovementType(command.amount().signum());
        HouseholdBagMovement movement = new HouseholdBagMovement(
            command.bagId(),
            command.amount(),
            movementType
        );
        movementRepository.save(movement);

        return new RegisterHouseholdBagMovementResult(
            bag.getId(),
            command.amount(),
            movementType,
            bag.getRemainingAmount(),
            bag.getUpdatedAt()
        );
    }

    private HouseholdBagMovementType resolveMovementType(int amountSign) {
        if (amountSign > 0) {
            return HouseholdBagMovementType.EXPENSE;
        }
        return HouseholdBagMovementType.ADJUSTMENT;
    }
}
