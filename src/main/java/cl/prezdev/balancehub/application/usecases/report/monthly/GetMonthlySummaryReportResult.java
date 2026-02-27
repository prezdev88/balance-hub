package cl.prezdev.balancehub.application.usecases.report.monthly;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import cl.prezdev.balancehub.domain.enums.SalarySnapshotStatus;

public record GetMonthlySummaryReportResult(
    String debtorId,
    String debtorName,
    String debtorEmail,
    int year,
    int month,
    BigDecimal monthlyFreeAmount,
    BigDecimal halfFreeAmount,
    BigDecimal totalInstallmentsAmount,
    BigDecimal salaryColumnAmount,
    SalarySnapshotStatus salaryStatus,
    Instant salaryPaidAt,
    List<MonthlySummaryInstallmentItem> installments
) {}
