package cl.prezdev.balancehub.application.usecases.debt.get;

import java.time.LocalDate;

public record GetDebtsCommand (
    String debtorId, 
    LocalDate startDate, 
    LocalDate endDate
) {}
