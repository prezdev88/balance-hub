package cl.prezdev.balancehub.application.usecases.salarysnapshot.pay;

import cl.prezdev.balancehub.application.usecases.salarysnapshot.SalarySnapshotItem;

public record PayMonthlySalaryResult(
    SalarySnapshotItem snapshot,
    boolean created
) {}
