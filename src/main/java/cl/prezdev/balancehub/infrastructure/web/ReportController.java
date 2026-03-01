package cl.prezdev.balancehub.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.GetMonthlySummaryReportInputPort;
import cl.prezdev.balancehub.application.usecases.report.monthly.GetMonthlySummaryReportCommand;
import cl.prezdev.balancehub.application.usecases.report.monthly.GetMonthlySummaryReportResult;
import cl.prezdev.balancehub.application.usecases.report.monthly.MonthlySummaryInstallmentItem;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final GetMonthlySummaryReportInputPort getMonthlySummaryReportUseCase;

    public ReportController(GetMonthlySummaryReportInputPort getMonthlySummaryReportUseCase) {
        this.getMonthlySummaryReportUseCase = getMonthlySummaryReportUseCase;
    }

    @GetMapping("/monthly-summary")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('DEBTOR') and #debtorId == authentication.principal.debtorId)")
    public ResponseEntity<MonthlySummaryHttpResponse> monthlySummary(
        @RequestParam String debtorId,
        @RequestParam int year,
        @RequestParam int month
    ) {
        var summary = getMonthlySummaryReportUseCase.execute(new GetMonthlySummaryReportCommand(debtorId, year, month));
        return ResponseEntity.ok(toHttpResponse(summary));
    }

    private MonthlySummaryHttpResponse toHttpResponse(GetMonthlySummaryReportResult summary) {
        return new MonthlySummaryHttpResponse(
            summary.debtorId(),
            summary.debtorName(),
            summary.debtorEmail(),
            summary.year(),
            summary.month(),
            summary.monthlyFreeAmount(),
            summary.halfFreeAmount(),
            summary.totalInstallmentsAmount(),
            summary.salaryColumnAmount(),
            summary.salaryStatus() != null ? summary.salaryStatus().name() : null,
            summary.salaryPaidAt(),
            summary.installments().stream().map(this::toHttpInstallment).toList()
        );
    }

    private MonthlySummaryInstallmentHttpItem toHttpInstallment(MonthlySummaryInstallmentItem item) {
        return new MonthlySummaryInstallmentHttpItem(
            item.installmentId(),
            item.debtDescription(),
            item.installmentNumber(),
            item.totalInstallments(),
            item.dueDate(),
            item.amount(),
            item.paid(),
            item.paidAt()
        );
    }

    public record MonthlySummaryHttpResponse(
        String debtorId,
        String debtorName,
        String debtorEmail,
        int year,
        int month,
        BigDecimal monthlyFreeAmount,
        BigDecimal halfFreeAmount,
        BigDecimal totalInstallmentsAmount,
        BigDecimal salaryColumnAmount,
        String salaryStatus,
        Instant salaryPaidAt,
        List<MonthlySummaryInstallmentHttpItem> installments
    ) {}

    public record MonthlySummaryInstallmentHttpItem(
        String installmentId,
        String debtDescription,
        int installmentNumber,
        int totalInstallments,
        LocalDate dueDate,
        BigDecimal amount,
        boolean paid,
        Instant paidAt
    ) {}
}
