package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbag.movement.RegisterHouseholdBagMovementCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.movement.RegisterHouseholdBagMovementResult;

public interface RegisterHouseholdBagMovementInputPort {
    RegisterHouseholdBagMovementResult execute(RegisterHouseholdBagMovementCommand command);
}
