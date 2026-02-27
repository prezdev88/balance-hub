package cl.prezdev.balancehub.application.ports.in;

import cl.prezdev.balancehub.application.usecases.report.monthly.GetMonthlySummaryReportCommand;
import cl.prezdev.balancehub.application.usecases.report.monthly.GetMonthlySummaryReportResult;

public interface GetMonthlySummaryReportInputPort {
    GetMonthlySummaryReportResult execute(GetMonthlySummaryReportCommand command);
}
