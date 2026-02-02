package cl.prezdev.balancehub.application.usecases.salary.create;

import java.math.BigDecimal;

public record CreateSalaryCommand (
    BigDecimal amount
) {}
