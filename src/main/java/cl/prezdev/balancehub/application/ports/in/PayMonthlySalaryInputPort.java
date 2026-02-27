package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.salarysnapshot.pay.PayMonthlySalaryCommand;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.pay.PayMonthlySalaryResult;

public interface PayMonthlySalaryInputPort {
    PayMonthlySalaryResult execute(PayMonthlySalaryCommand command);
}
