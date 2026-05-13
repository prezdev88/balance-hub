package cl.prezdev.balancehub.application.usecases.householdbag.list;

import java.math.BigDecimal;
import java.util.List;

import cl.prezdev.balancehub.application.ports.in.ListHouseholdBagsInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.application.usecases.householdbag.HouseholdBagDetails;
import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;

public class ListHouseholdBagsUseCase implements ListHouseholdBagsInputPort {

    private final HouseholdBudgetRepository repository;

    public ListHouseholdBagsUseCase(HouseholdBudgetRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("householdBudgetRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public ListHouseholdBagsResult execute(ListHouseholdBagsCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command cannot be null");
        }

        List<HouseholdBagDetails> bags = repository.findAllConfigs().stream()
            .map(this::toDetails)
            .toList();
        return new ListHouseholdBagsResult(bags);
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
}
