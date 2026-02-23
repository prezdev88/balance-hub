package cl.prezdev.balancehub.infrastructure.web;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.usecases.debt.create.CreateDebtResult;
import cl.prezdev.balancehub.application.usecases.debt.create.CreateDebtUseCase;
import cl.prezdev.balancehub.application.usecases.debt.create.command.CreateDebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.DebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.InstallmentCommand;
import cl.prezdev.balancehub.application.usecases.debt.get.GetDebtsCommand;
import cl.prezdev.balancehub.application.usecases.debt.get.GetDebtsResult;
import cl.prezdev.balancehub.application.usecases.debt.get.GetDebtsUseCase;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final CreateDebtUseCase createDebtUseCase;
    private final GetDebtsUseCase getDebtsUseCase;

    public DebtController(CreateDebtUseCase createDebtUseCase, GetDebtsUseCase getDebtsUseCase) {
        this.createDebtUseCase = createDebtUseCase;
        this.getDebtsUseCase = getDebtsUseCase;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CreateDebtResult> create(@RequestBody CreateDebtRequest request) {
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
            .body(result);
    }

    @GetMapping
    public ResponseEntity<GetDebtsResult> getByDebtorAndRange(
        @RequestParam String debtorId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(getDebtsUseCase.execute(new GetDebtsCommand(debtorId, startDate, endDate)));
    }

    public record CreateDebtRequest(DebtPayload debt, InstallmentsPayload installments) {}

    public record DebtPayload(String debtorId, String description, java.math.BigDecimal totalAmount) {}

    public record InstallmentsPayload(
        int installmentsCount,
        java.math.BigDecimal installmentAmount,
        LocalDate firstInstallmentDueDate
    ) {}
}
