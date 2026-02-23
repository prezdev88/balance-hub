package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.salary.create.CreateSalaryCommand;
import cl.prezdev.balancehub.application.usecases.salary.create.CreateSalaryResult;

public interface CreateSalaryInputPort {
    CreateSalaryResult execute(CreateSalaryCommand command);
}
