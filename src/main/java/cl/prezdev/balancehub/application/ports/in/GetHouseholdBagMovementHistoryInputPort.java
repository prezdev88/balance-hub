package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbag.history.GetHouseholdBagMovementHistoryCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.history.GetHouseholdBagMovementHistoryResult;

public interface GetHouseholdBagMovementHistoryInputPort {
    GetHouseholdBagMovementHistoryResult execute(GetHouseholdBagMovementHistoryCommand command);
}
