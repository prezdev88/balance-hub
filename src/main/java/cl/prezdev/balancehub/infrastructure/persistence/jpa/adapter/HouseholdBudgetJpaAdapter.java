package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.HouseholdBudgetRepository;
import cl.prezdev.balancehub.domain.HouseholdBudgetConfig;
import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.HouseholdBagJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.HouseholdBagJpaRepository;
import cl.prezdev.balancehub.domain.exception.InvalidHouseholdBudgetException;

@Repository
public class HouseholdBudgetJpaAdapter implements HouseholdBudgetRepository {

    private static final String VEGETABLES_BAG_NAME = "Verduras";
    private static final String GROCERIES_BAG_NAME = "Mercadería";

    private final HouseholdBagJpaRepository bagRepository;

    public HouseholdBudgetJpaAdapter(HouseholdBagJpaRepository bagRepository) {
        this.bagRepository = bagRepository;
    }

    @Override
    public Optional<HouseholdBudgetConfig> findConfigByCategory(HouseholdBudgetCategory category) {
        String bagName = getCompatibilityBagName(category);
        return bagRepository.findByName(bagName).map(this::toDomainConfig);
    }

    @Override
    public Optional<HouseholdBudgetConfig> findById(String id) {
        return bagRepository.findByIdAndActiveTrue(id).map(this::toDomainConfig);
    }

    @Override
    public Optional<HouseholdBudgetConfig> findByName(String name) {
        return bagRepository.findByName(name).map(this::toDomainConfig);
    }

    @Override
    public List<HouseholdBudgetConfig> findAllConfigs() {
        return bagRepository.findAllByActiveTrueOrderByNameAsc().stream().map(this::toDomainConfig).toList();
    }

    @Override
    public void saveConfig(HouseholdBudgetConfig config) {
        HouseholdBagJpaEntity entity = toEntityConfig(config);
        bagRepository.save(entity);
    }

    @Override
    public void save(HouseholdBudgetConfig config) {
        saveConfig(config);
    }

    @Override
    public void deactivateBag(String id) {
        HouseholdBagJpaEntity entity = bagRepository.findById(id)
            .orElseThrow(() -> new InvalidHouseholdBudgetException("Bag " + id + " not found"));
        if (!entity.isActive()) {
            throw new InvalidHouseholdBudgetException("Bag " + id + " is already inactive");
        }
        entity.setActive(false);
        bagRepository.save(entity);
    }

    private HouseholdBudgetConfig toDomainConfig(HouseholdBagJpaEntity entity) {
        return new HouseholdBudgetConfig(
            entity.getId(),
            entity.getName(),
            entity.getEmoji(),
            entity.getMonthlyAmount(),
            entity.getRemainingAmount(),
            entity.getUpdatedAt()
        );
    }

    private HouseholdBagJpaEntity toEntityConfig(HouseholdBudgetConfig config) {
        return new HouseholdBagJpaEntity(
            config.getId(),
            config.getName(),
            config.getEmoji(),
            config.getMonthlyAmount(),
            config.getRemainingAmount(),
            config.getUpdatedAt()
        );
    }

    private String getCompatibilityBagName(HouseholdBudgetCategory category) {
        return switch (category) {
            case VEGETABLES -> VEGETABLES_BAG_NAME;
            case GROCERIES -> GROCERIES_BAG_NAME;
        };
    }
}
