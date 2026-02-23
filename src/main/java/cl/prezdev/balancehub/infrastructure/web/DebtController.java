package cl.prezdev.balancehub.infrastructure.web;

import java.net.URI;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.CreateDebtInputPort;
import cl.prezdev.balancehub.application.ports.in.GetDebtsInputPort;
import cl.prezdev.balancehub.application.usecases.debt.create.CreateDebtResult;
import cl.prezdev.balancehub.application.usecases.debt.create.command.CreateDebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.DebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.InstallmentCommand;
import cl.prezdev.balancehub.application.usecases.debt.get.DebtItem;
import cl.prezdev.balancehub.application.usecases.debt.get.GetDebtsCommand;
import cl.prezdev.balancehub.application.usecases.debt.get.GetDebtsResult;
import cl.prezdev.balancehub.application.usecases.debt.get.InstallmentItem;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final CreateDebtInputPort createDebtUseCase;
    private final GetDebtsInputPort getDebtsUseCase;

    public DebtController(CreateDebtInputPort createDebtUseCase, GetDebtsInputPort getDebtsUseCase) {
        this.createDebtUseCase = createDebtUseCase;
        this.getDebtsUseCase = getDebtsUseCase;
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

    @GetMapping
    public ResponseEntity<GetDebtsHttpResponse> getByDebtorAndRange(
        @RequestParam String debtorId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        GetDebtsResult result = getDebtsUseCase.execute(new GetDebtsCommand(debtorId, startDate, endDate));
        return ResponseEntity.ok(toHttpResponse(result));
    }

    private static GetDebtsHttpResponse toHttpResponse(GetDebtsResult result) {
        return new GetDebtsHttpResponse(
            result.id(),
            result.name(),
            result.email(),
            result.debts().stream().map(DebtController::toHttpDebtItem).toList()
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

    public record GetDebtsHttpResponse(
        String id,
        String name,
        String email,
        List<DebtHttpItem> debts
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
