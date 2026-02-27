package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.GetMonthlySalarySnapshotInputPort;
import cl.prezdev.balancehub.application.ports.in.PayMonthlySalaryInputPort;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.SalarySnapshotItem;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.get.GetMonthlySalarySnapshotCommand;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.pay.PayMonthlySalaryCommand;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.pay.PayMonthlySalaryResult;
import cl.prezdev.balancehub.domain.enums.SalarySnapshotStatus;

@RestController
@RequestMapping("/api/salary-snapshots")
public class SalarySnapshotController {

    private final GetMonthlySalarySnapshotInputPort getMonthlySalarySnapshotUseCase;
    private final PayMonthlySalaryInputPort payMonthlySalaryUseCase;

    public SalarySnapshotController(
        GetMonthlySalarySnapshotInputPort getMonthlySalarySnapshotUseCase,
        PayMonthlySalaryInputPort payMonthlySalaryUseCase
    ) {
        this.getMonthlySalarySnapshotUseCase = getMonthlySalarySnapshotUseCase;
        this.payMonthlySalaryUseCase = payMonthlySalaryUseCase;
    }

    @GetMapping
    public ResponseEntity<SalarySnapshotHttpResponse> getByPeriod(
        @RequestParam String debtorId,
        @RequestParam int year,
        @RequestParam int month
    ) {
        var result = getMonthlySalarySnapshotUseCase.execute(new GetMonthlySalarySnapshotCommand(debtorId, year, month));
        return ResponseEntity.ok(toHttpResponse(result.snapshot()));
    }

    @PostMapping("/pay")
    public ResponseEntity<PayMonthlySalaryHttpResponse> pay(@RequestBody PayMonthlySalaryRequest request) {
        PayMonthlySalaryResult result = payMonthlySalaryUseCase.execute(
            new PayMonthlySalaryCommand(request.debtorId(), request.year(), request.month(), request.paymentDate())
        );
        return ResponseEntity.ok(new PayMonthlySalaryHttpResponse(result.created(), toHttpResponse(result.snapshot())));
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
}
