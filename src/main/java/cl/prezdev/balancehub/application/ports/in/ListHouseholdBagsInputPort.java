package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.householdbag.list.ListHouseholdBagsCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.list.ListHouseholdBagsResult;

public interface ListHouseholdBagsInputPort {
    ListHouseholdBagsResult execute(ListHouseholdBagsCommand command);
}
