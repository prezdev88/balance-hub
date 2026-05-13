package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbag.create.CreateHouseholdBagCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.create.CreateHouseholdBagResult;

public interface CreateHouseholdBagInputPort {
    CreateHouseholdBagResult execute(CreateHouseholdBagCommand command);
}
