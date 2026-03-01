package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.GetMonthlySalarySnapshotInputPort;
import cl.prezdev.balancehub.application.ports.in.GetMonthlyFreeAmountInputPort;
import cl.prezdev.balancehub.application.ports.in.GetUnpaidInstallmentsByMonthInputPort;
import cl.prezdev.balancehub.application.ports.in.PayMonthlySalaryInputPort;
import cl.prezdev.balancehub.application.usecases.financialplan.GetMonthlyFreeAmountCommand;
import cl.prezdev.balancehub.application.usecases.installment.unpaid.GetUnpaidInstallmentsByMonthCommand;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.SalarySnapshotItem;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.get.GetMonthlySalarySnapshotCommand;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.pay.PayMonthlySalaryCommand;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.pay.PayMonthlySalaryResult;
import cl.prezdev.balancehub.application.exception.MonthlySalarySnapshotNotFoundException;
import cl.prezdev.balancehub.domain.enums.SalarySnapshotStatus;

@RestController
@RequestMapping("/api/salary-snapshots")
public class SalarySnapshotController {

    private final GetMonthlySalarySnapshotInputPort getMonthlySalarySnapshotUseCase;
    private final PayMonthlySalaryInputPort payMonthlySalaryUseCase;
    private final GetUnpaidInstallmentsByMonthInputPort getUnpaidInstallmentsByMonthUseCase;
    private final GetMonthlyFreeAmountInputPort getMonthlyFreeAmountUseCase;

    public SalarySnapshotController(
        GetMonthlySalarySnapshotInputPort getMonthlySalarySnapshotUseCase,
        PayMonthlySalaryInputPort payMonthlySalaryUseCase,
        GetUnpaidInstallmentsByMonthInputPort getUnpaidInstallmentsByMonthUseCase,
        GetMonthlyFreeAmountInputPort getMonthlyFreeAmountUseCase
    ) {
        this.getMonthlySalarySnapshotUseCase = getMonthlySalarySnapshotUseCase;
        this.payMonthlySalaryUseCase = payMonthlySalaryUseCase;
        this.getUnpaidInstallmentsByMonthUseCase = getUnpaidInstallmentsByMonthUseCase;
        this.getMonthlyFreeAmountUseCase = getMonthlyFreeAmountUseCase;
    }

    @PostMapping("/pay")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PayMonthlySalaryHttpResponse> pay(@RequestBody PayMonthlySalaryRequest request) {
        PayMonthlySalaryResult result = payMonthlySalaryUseCase.execute(
            new PayMonthlySalaryCommand(request.debtorId(), request.year(), request.month(), request.paymentDate())
        );
        return ResponseEntity.ok(new PayMonthlySalaryHttpResponse(result.created(), toHttpResponse(result.snapshot())));
    }

    @GetMapping("/preview")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('DEBTOR') and #debtorId == authentication.principal.debtorId)")
    public ResponseEntity<SalaryPreviewHttpResponse> getPreview(
        @RequestParam String debtorId,
        @RequestParam int year,
        @RequestParam int month
    ) {
        var unpaidResult = getUnpaidInstallmentsByMonthUseCase.execute(
            new GetUnpaidInstallmentsByMonthCommand(debtorId, year, month)
        );
        var freeAmountResult = getMonthlyFreeAmountUseCase.execute(new GetMonthlyFreeAmountCommand(year));
        BigDecimal halfFreeAmount = freeAmountResult.monthlyFreeAmount().divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        BigDecimal salaryPreviewAmount = halfFreeAmount.subtract(unpaidResult.totalAmount());

        SalarySnapshotHttpResponse snapshot = null;
        try {
            var snapshotResult = getMonthlySalarySnapshotUseCase.execute(new GetMonthlySalarySnapshotCommand(debtorId, year, month));
            snapshot = toHttpResponse(snapshotResult.snapshot());
        } catch (MonthlySalarySnapshotNotFoundException ignored) {
            snapshot = null;
        }

        return ResponseEntity.ok(new SalaryPreviewHttpResponse(
            debtorId,
            year,
            month,
            unpaidResult.totalAmount(),
            salaryPreviewAmount,
            snapshot
        ));
    }

    private static SalarySnapshotHttpResponse toHttpResponse(SalarySnapshotItem item) {
        return new SalarySnapshotHttpResponse(
            item.id(),
            item.debtorId(),
            item.year(),
            item.month(),
            item.monthlyFreeAmount(),
            item.halfFreeAmount(),
            item.totalInstallmentsAmount(),
            item.salaryColumnAmount(),
            item.status(),
            item.createdAt(),
            item.paidAt()
        );
    }

    public record PayMonthlySalaryRequest(String debtorId, int year, int month, Instant paymentDate) {}

    public record PayMonthlySalaryHttpResponse(boolean created, SalarySnapshotHttpResponse snapshot) {}

    public record SalarySnapshotHttpResponse(
        String id,
        String debtorId,
        int year,
        int month,
        BigDecimal monthlyFreeAmount,
        BigDecimal halfFreeAmount,
        BigDecimal totalInstallmentsAmount,
        BigDecimal salaryColumnAmount,
        SalarySnapshotStatus status,
        Instant createdAt,
        Instant paidAt
    ) {}

    public record SalaryPreviewHttpResponse(
        String debtorId,
        int year,
        int month,
        BigDecimal totalUnpaidInstallments,
        BigDecimal salaryPreviewAmount,
        SalarySnapshotHttpResponse snapshot
    ) {}
}
