package cl.prezdev.balancehub.application.usecases.householdbag.create;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import cl.prezdev.balancehub.application.ports.in.CreateHouseholdBagInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.application.usecases.householdbag.HouseholdBagDetails;
import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

public class CreateHouseholdBagUseCase implements CreateHouseholdBagInputPort {

    private final HouseholdBudgetRepository repository;

    public CreateHouseholdBagUseCase(HouseholdBudgetRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("householdBudgetRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public CreateHouseholdBagResult execute(CreateHouseholdBagCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        String normalizedName = normalizeRequired(command.name(), "Bag name cannot be null or blank");
        String normalizedEmoji = normalizeRequired(command.emoji(), "Bag emoji cannot be null or blank");
        BigDecimal monthlyAmount = requirePositiveAmount(command.monthlyAmount(), "Monthly amount must be greater than zero");
        Instant now = Instant.now();
        String id = UUID.randomUUID().toString();
        HouseholdBudgetConfig bag = new HouseholdBudgetConfig(
            id,
            normalizedName,
            normalizedEmoji,
            monthlyAmount,
            monthlyAmount,
            now
        );
        repository.save(bag);

        HouseholdBagDetails details = toDetails(bag);
        return new CreateHouseholdBagResult(details);
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

    private String normalizeRequired(String value, String errorMessage) {
        if (value == null) {
            throw new InvalidHouseholdBudgetException(errorMessage);
        }
        String normalizedValue = value.trim();
        if (normalizedValue.isEmpty()) {
            throw new InvalidHouseholdBudgetException(errorMessage);
        }
        return normalizedValue;
    }

    private BigDecimal requirePositiveAmount(BigDecimal amount, String errorMessage) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidHouseholdBudgetException(errorMessage);
        }
        return amount;
    }
}
