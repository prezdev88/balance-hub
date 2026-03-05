package cl.prezdev.balancehub.application.ports.out;

import java.util.List;
import java.util.Optional;

import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;
import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

public interface HouseholdBudgetRepository {
    Optional<HouseholdBudgetConfig> findConfigByCategory(HouseholdBudgetCategory category);

    List<HouseholdBudgetConfig> findAllConfigs();

    void saveConfig(HouseholdBudgetConfig config);
}
