package cl.prezdev.balancehub.infrastructure.web;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.GetMonthlySummaryReportInputPort;
import cl.prezdev.balancehub.application.usecases.report.monthly.GetMonthlySummaryReportCommand;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final GetMonthlySummaryReportInputPort getMonthlySummaryReportUseCase;

    public ReportController(GetMonthlySummaryReportInputPort getMonthlySummaryReportUseCase) {
        this.getMonthlySummaryReportUseCase = getMonthlySummaryReportUseCase;
    }

    @GetMapping(value = "/monthly-summary.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('DEBTOR') and #debtorId == authentication.principal.debtorId)")
    public ResponseEntity<byte[]> monthlySummaryPdf(
        @RequestParam String debtorId,
        @RequestParam int year,
        @RequestParam int month
    ) {
        var summary = getMonthlySummaryReportUseCase.execute(new GetMonthlySummaryReportCommand(debtorId, year, month));
        var pdf = MonthlySummaryReportPdfGenerator.generate(summary);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
            ContentDisposition.attachment().filename("monthly-summary-" + debtorId + "-" + year + "-" + month + ".pdf").build()
        );

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
