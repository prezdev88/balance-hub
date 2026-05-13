package cl.prezdev.balancehub.application.usecases.householdbag.history;

import java.util.List;

public record GetHouseholdBagMovementHistoryResult(
    String bagId,
    List<HouseholdBagMovementHistoryItem> movements
) {}
