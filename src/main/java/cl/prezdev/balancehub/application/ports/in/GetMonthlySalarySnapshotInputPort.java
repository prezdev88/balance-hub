package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.salarysnapshot.get.GetMonthlySalarySnapshotCommand;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.get.GetMonthlySalarySnapshotResult;

public interface GetMonthlySalarySnapshotInputPort {
    GetMonthlySalarySnapshotResult execute(GetMonthlySalarySnapshotCommand command);
}
