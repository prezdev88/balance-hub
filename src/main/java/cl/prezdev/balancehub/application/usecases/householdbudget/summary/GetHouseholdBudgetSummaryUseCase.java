package cl.prezdev.balancehub.application.usecases.householdbudget.summary;

import java.math.BigDecimal;
import java.util.Arrays;

import cl.prezdev.balancehub.application.ports.in.GetHouseholdBudgetSummaryInputPort;
import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

public class GetHouseholdBudgetSummaryUseCase implements GetHouseholdBudgetSummaryInputPort {

    private final HouseholdBudgetRepository repository;

    public GetHouseholdBudgetSummaryUseCase(HouseholdBudgetRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("HouseholdBudgetRepository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public GetHouseholdBudgetSummaryResult execute(GetHouseholdBudgetSummaryCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var configsByCategory = repository.findAllConfigs().stream().collect(
            java.util.stream.Collectors.toMap(
                cl.prezdev.balancehub.domain.HouseholdBudgetConfig::getCategory,
                config -> config
            )
        );

        var items = Arrays.stream(HouseholdBudgetCategory.values())
            .map(category -> {
                var config = configsByCategory.get(category);
                BigDecimal monthlyAmount = config != null ? config.getMonthlyAmount() : BigDecimal.ZERO;
                BigDecimal remainingAmount = config != null ? config.getRemainingAmount() : BigDecimal.ZERO;
                BigDecimal spentAmount = monthlyAmount.subtract(remainingAmount);
                return new HouseholdBudgetSummaryItem(category, monthlyAmount, spentAmount, remainingAmount);
            })
            .toList();

        return new GetHouseholdBudgetSummaryResult(items);
    }
}
