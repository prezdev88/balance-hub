package cl.prezdev.balancehub.application.usecases.householdbag.history;

public record GetHouseholdBagMovementHistoryCommand(String bagId, int page, int size) {
    public GetHouseholdBagMovementHistoryCommand(String bagId) {
        this(bagId, 0, 3);
    }
}
