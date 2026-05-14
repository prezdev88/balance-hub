package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbag.deactivate.DeactivateHouseholdBagCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.deactivate.DeactivateHouseholdBagResult;

public interface DeactivateHouseholdBagInputPort {
    DeactivateHouseholdBagResult execute(DeactivateHouseholdBagCommand command);
}
