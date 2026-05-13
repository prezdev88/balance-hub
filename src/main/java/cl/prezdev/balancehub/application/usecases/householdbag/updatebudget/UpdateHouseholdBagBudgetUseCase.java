package cl.prezdev.balancehub.application.usecases.householdbag.updatebudget;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.application.ports.in.UpdateHouseholdBagBudgetInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.application.usecases.householdbag.HouseholdBagDetails;
import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class UpdateHouseholdBagBudgetUseCase implements UpdateHouseholdBagBudgetInputPort {

    private final HouseholdBudgetRepository repository;

    public UpdateHouseholdBagBudgetUseCase(HouseholdBudgetRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("householdBudgetRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public UpdateHouseholdBagBudgetResult execute(UpdateHouseholdBagBudgetCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        BigDecimal monthlyAmount = requirePositiveAmount(command.monthlyAmount());
        HouseholdBudgetConfig bag = repository.findById(command.bagId())
            .orElseThrow(() -> new InvalidHouseholdBudgetException("Bag " + command.bagId() + " not found"));

        Instant now = Instant.now();
        bag.updateMonthlyAmount(monthlyAmount, now);
        repository.save(bag);

        HouseholdBagDetails details = toDetails(bag);
        return new UpdateHouseholdBagBudgetResult(details);
    }

    private HouseholdBagDetails toDetails(HouseholdBudgetConfig bag) {
        BigDecimal spentAmount = bag.getMonthlyAmount().subtract(bag.getRemainingAmount());
        return new HouseholdBagDetails(
            bag.getId(),
            bag.getName(),
            bag.getEmoji(),
            bag.getMonthlyAmount(),
            spentAmount,
            bag.getRemainingAmount(),
            bag.getUpdatedAt()
        );
    }

    private BigDecimal requirePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidHouseholdBudgetException("Monthly amount must be greater than zero");
        }
        return amount;
    }
}
