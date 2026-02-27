package cl.prezdev.balancehub.application.usecases.savingsgoal.create;

import java.math.BigDecimal;

public record CreateSavingsGoalCommand(BigDecimal amount) {}
