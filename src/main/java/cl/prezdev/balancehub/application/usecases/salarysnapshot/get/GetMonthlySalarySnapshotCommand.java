package cl.prezdev.balancehub.application.usecases.salarysnapshot.get;

public record GetMonthlySalarySnapshotCommand(
    String debtorId,
    int year,
    int month
) {}
