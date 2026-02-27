package cl.prezdev.balancehub.application.usecases.report.monthly;

public record GetMonthlySummaryReportCommand(
    String debtorId,
    int year,
    int month
) {}
