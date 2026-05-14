package cl.prezdev.balancehub.application.ports.out;

import java.util.List;

import cl.prezdev.balancehub.application.usecases.householdbag.history.HouseholdBagMovementPaginatedResult;
import cl.prezdev.balancehub.domain.HouseholdBagMovement;

public interface HouseholdBagMovementRepository {
    void save(HouseholdBagMovement movement);

    List<HouseholdBagMovement> findByBagId(String bagId);

    HouseholdBagMovementPaginatedResult findByBagIdPaginated(String bagId, int page, int size);
}
