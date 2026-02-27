package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.savingsgoal.create.CreateSavingsGoalCommand;
import cl.prezdev.balancehub.application.usecases.savingsgoal.create.CreateSavingsGoalResult;

public interface CreateSavingsGoalInputPort {
    CreateSavingsGoalResult execute(CreateSavingsGoalCommand command);
}
