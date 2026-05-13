package cl.prezdev.balancehub.application.usecases.householdbag.list;

import java.util.List;

import cl.prezdev.balancehub.application.usecases.householdbag.HouseholdBagDetails;

public record ListHouseholdBagsResult(List<HouseholdBagDetails> bags) {}
