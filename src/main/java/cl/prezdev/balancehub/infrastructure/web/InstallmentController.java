package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.GetUnpaidInstallmentsByMonthInputPort;
import cl.prezdev.balancehub.application.ports.in.PayInstallmentInputPort;
import cl.prezdev.balancehub.application.usecases.installment.pay.PayInstallmentCommand;
import cl.prezdev.balancehub.application.usecases.installment.unpaid.GetUnpaidInstallmentsByMonthCommand;
import cl.prezdev.balancehub.application.usecases.installment.unpaid.GetUnpaidInstallmentsByMonthResult;
import cl.prezdev.balancehub.application.usecases.installment.unpaid.UnpaidInstallmentItem;

@RestController
@RequestMapping("/api/installments")
public class InstallmentController {

    private final PayInstallmentInputPort payInstallmentUseCase;
    private final GetUnpaidInstallmentsByMonthInputPort getUnpaidInstallmentsByMonthUseCase;

    public InstallmentController(
        PayInstallmentInputPort payInstallmentUseCase,
        GetUnpaidInstallmentsByMonthInputPort getUnpaidInstallmentsByMonthUseCase
    ) {
        this.payInstallmentUseCase = payInstallmentUseCase;
        this.getUnpaidInstallmentsByMonthUseCase = getUnpaidInstallmentsByMonthUseCase;
    }

    @PatchMapping("/{installmentId}/pay")
    public ResponseEntity<Void> pay(@PathVariable String installmentId, @RequestBody PayInstallmentRequest request) {
        payInstallmentUseCase.execute(new PayInstallmentCommand(installmentId, request.paymentDate()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unpaid")
    public ResponseEntity<GetUnpaidInstallmentsByMonthHttpResponse> getUnpaidByMonth(
        @RequestParam String debtorId,
        @RequestParam int year,
        @RequestParam int month
    ) {
        GetUnpaidInstallmentsByMonthResult result = getUnpaidInstallmentsByMonthUseCase.execute(
            new GetUnpaidInstallmentsByMonthCommand(debtorId, year, month)
        );

        return ResponseEntity.ok(
            new GetUnpaidInstallmentsByMonthHttpResponse(
                result.debtorId(),
                result.debtorName(),
                result.debtorEmail(),
                result.year(),
                result.month(),
                result.totalAmount(),
                result.installments().stream().map(InstallmentController::toHttpItem).toList()
            )
        );
    }

    private static UnpaidInstallmentHttpItem toHttpItem(UnpaidInstallmentItem item) {
        return new UnpaidInstallmentHttpItem(
            item.installmentId(),
            item.debtId(),
            item.debtDescription(),
            item.installmentNumber(),
            item.totalInstallments(),
            item.dueDate(),
            item.amount(),
            item.paid(),
            item.paidAt()
        );
    }

    public record PayInstallmentRequest(Instant paymentDate) {}

    public record GetUnpaidInstallmentsByMonthHttpResponse(
        String debtorId,
        String debtorName,
        String debtorEmail,
        int year,
        int month,
        BigDecimal totalAmount,
        List<UnpaidInstallmentHttpItem> installments
    ) {}

    public record UnpaidInstallmentHttpItem(
        String installmentId,
        String debtId,
        String debtDescription,
        int installmentNumber,
        int totalInstallments,
        LocalDate dueDate,
        BigDecimal amount,
        boolean paid,
        Instant paidAt
    ) {}
}
