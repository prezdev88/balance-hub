package cl.prezdev.balancehub.application.usecases.householdbag.history;

import java.util.List;

import cl.prezdev.balancehub.domain.HouseholdBagMovement;

public record HouseholdBagMovementPaginatedResult(
    List<HouseholdBagMovement> movements,
    int page,
    int size,
    int totalPages,
    long totalElements
) {}
