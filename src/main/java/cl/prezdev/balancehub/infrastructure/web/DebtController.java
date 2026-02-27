package cl.prezdev.balancehub.infrastructure.web;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.CreateDebtInputPort;
import cl.prezdev.balancehub.application.ports.in.DeleteDebtInputPort;
import cl.prezdev.balancehub.application.ports.in.GetDebtDetailInputPort;
import cl.prezdev.balancehub.application.usecases.debt.create.CreateDebtResult;
import cl.prezdev.balancehub.application.usecases.debt.create.command.CreateDebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.DebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.InstallmentCommand;
import cl.prezdev.balancehub.application.usecases.debt.delete.DeleteDebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.get.DebtItem;
import cl.prezdev.balancehub.application.usecases.debt.get.InstallmentItem;
import cl.prezdev.balancehub.application.usecases.debt.getdetail.GetDebtDetailCommand;
import cl.prezdev.balancehub.application.usecases.debt.getdetail.GetDebtDetailResult;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final CreateDebtInputPort createDebtUseCase;
    private final DeleteDebtInputPort deleteDebtUseCase;
    private final GetDebtDetailInputPort getDebtDetailUseCase;

    public DebtController(
        CreateDebtInputPort createDebtUseCase,
        DeleteDebtInputPort deleteDebtUseCase,
        GetDebtDetailInputPort getDebtDetailUseCase
    ) {
        this.createDebtUseCase = createDebtUseCase;
        this.deleteDebtUseCase = deleteDebtUseCase;
        this.getDebtDetailUseCase = getDebtDetailUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateDebtHttpResponse> create(@RequestBody CreateDebtRequest request) {
        CreateDebtResult result = createDebtUseCase.execute(
            new CreateDebtCommand(
                new DebtCommand(
                    request.debt().debtorId(),
                    request.debt().description(),
                    request.debt().totalAmount()
                ),
                new InstallmentCommand(
                    request.installments().installmentsCount(),
                    request.installments().installmentAmount(),
                    request.installments().firstInstallmentDueDate()
                )
            )
        );

        return ResponseEntity
            .created(URI.create("/api/debts/" + result.debtId()))
            .body(new CreateDebtHttpResponse(result.debtId()));
    }

    @GetMapping("/{debtId}")
    public ResponseEntity<GetDebtDetailHttpResponse> getDebtDetail(@PathVariable String debtId) {
        GetDebtDetailResult result = getDebtDetailUseCase.execute(new GetDebtDetailCommand(debtId));
        return ResponseEntity.ok(toHttpResponse(result));
    }

    @DeleteMapping("/{debtId}")
    public ResponseEntity<Void> delete(@PathVariable String debtId) {
        deleteDebtUseCase.execute(new DeleteDebtCommand(debtId));
        return ResponseEntity.noContent().build();
    }

    private static GetDebtDetailHttpResponse toHttpResponse(GetDebtDetailResult result) {
        return new GetDebtDetailHttpResponse(
            result.id(),
            result.name(),
            result.email(),
            toHttpDebtItem(result.debt())
        );
    }

    private static DebtHttpItem toHttpDebtItem(DebtItem item) {
        return new DebtHttpItem(
            item.id(),
            item.description(),
            item.totalAmount(),
            item.createdAt(),
            item.settled(),
            item.installments().stream().map(DebtController::toHttpInstallmentItem).toList()
        );
    }

    private static InstallmentHttpItem toHttpInstallmentItem(InstallmentItem item) {
        return new InstallmentHttpItem(item.id(), item.number(), item.dueDate(), item.paidAt(), item.amount());
    }

    public record CreateDebtRequest(DebtPayload debt, InstallmentsPayload installments) {}

    public record DebtPayload(String debtorId, String description, java.math.BigDecimal totalAmount) {}

    public record InstallmentsPayload(
        int installmentsCount,
        java.math.BigDecimal installmentAmount,
        LocalDate firstInstallmentDueDate
    ) {}

    public record CreateDebtHttpResponse(String debtId) {}

    public record GetDebtDetailHttpResponse(
        String id,
        String name,
        String email,
        DebtHttpItem debt
    ) {}

    public record DebtHttpItem(
        String id,
        String description,
        java.math.BigDecimal totalAmount,
        Instant createdAt,
        boolean settled,
        List<InstallmentHttpItem> installments
    ) {}

    public record InstallmentHttpItem(
        String id,
        int number,
        LocalDate dueDate,
        Instant paidAt,
        java.math.BigDecimal amount
    ) {}
}
