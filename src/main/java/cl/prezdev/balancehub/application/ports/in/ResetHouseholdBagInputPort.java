package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbag.reset.ResetHouseholdBagCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.reset.ResetHouseholdBagResult;

public interface ResetHouseholdBagInputPort {
    ResetHouseholdBagResult execute(ResetHouseholdBagCommand command);
}
