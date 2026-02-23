package cl.prezdev.balancehub.infrastructure.web;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.PayInstallmentInputPort;
import cl.prezdev.balancehub.application.usecases.installment.pay.PayInstallmentCommand;

@RestController
@RequestMapping("/api/installments")
public class InstallmentController {

    private final PayInstallmentInputPort payInstallmentUseCase;

    public InstallmentController(PayInstallmentInputPort payInstallmentUseCase) {
        this.payInstallmentUseCase = payInstallmentUseCase;
    }

    @PatchMapping("/{installmentId}/pay")
    public ResponseEntity<Void> pay(@PathVariable String installmentId, @RequestBody PayInstallmentRequest request) {
        payInstallmentUseCase.execute(new PayInstallmentCommand(installmentId, request.paymentDate()));
        return ResponseEntity.noContent().build();
    }

    public record PayInstallmentRequest(Instant paymentDate) {}
}
