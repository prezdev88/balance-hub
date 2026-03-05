package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;
import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.HouseholdBudgetConfigJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.HouseholdBudgetConfigJpaRepository;

@Repository
public class HouseholdBudgetJpaAdapter implements HouseholdBudgetRepository {

    private final HouseholdBudgetConfigJpaRepository configRepository;

    public HouseholdBudgetJpaAdapter(HouseholdBudgetConfigJpaRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public Optional<HouseholdBudgetConfig> findConfigByCategory(HouseholdBudgetCategory category) {
        return configRepository.findById(category).map(this::toDomainConfig);
    }

    @Override
    public List<HouseholdBudgetConfig> findAllConfigs() {
        return configRepository.findAll().stream().map(this::toDomainConfig).toList();
    }

    @Override
    public void saveConfig(HouseholdBudgetConfig config) {
        configRepository.save(toEntityConfig(config));
    }

    private HouseholdBudgetConfig toDomainConfig(HouseholdBudgetConfigJpaEntity entity) {
        return new HouseholdBudgetConfig(
            entity.getCategory(),
            entity.getMonthlyAmount(),
            entity.getRemainingAmount(),
            entity.getUpdatedAt()
        );
    }

    private HouseholdBudgetConfigJpaEntity toEntityConfig(HouseholdBudgetConfig config) {
        return new HouseholdBudgetConfigJpaEntity(
            config.getCategory(),
            config.getMonthlyAmount(),
            config.getRemainingAmount(),
            config.getUpdatedAt()
        );
    }
}
