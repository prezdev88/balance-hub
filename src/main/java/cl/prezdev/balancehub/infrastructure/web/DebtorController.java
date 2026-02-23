package cl.prezdev.balancehub.infrastructure.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorCommand;
import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorResult;
import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorUseCase;
import cl.prezdev.balancehub.application.usecases.debtor.list.ListDebtorsResult;
import cl.prezdev.balancehub.application.usecases.debtor.list.ListDebtorsUseCase;

@RestController
@RequestMapping("/api/debtors")
public class DebtorController {

    private final CreateDebtorUseCase createDebtorUseCase;
    private final ListDebtorsUseCase listDebtorsUseCase;

    public DebtorController(CreateDebtorUseCase createDebtorUseCase, ListDebtorsUseCase listDebtorsUseCase) {
        this.createDebtorUseCase = createDebtorUseCase;
        this.listDebtorsUseCase = listDebtorsUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateDebtorResult> create(@RequestBody CreateDebtorRequest request) {
        CreateDebtorResult result = createDebtorUseCase.execute(
            new CreateDebtorCommand(request.name(), request.email())
        );

        return ResponseEntity
            .created(URI.create("/api/debtors/" + result.debtorId()))
            .body(result);
    }

    @GetMapping
    public ResponseEntity<ListDebtorsResult> list() {
        return ResponseEntity.ok(listDebtorsUseCase.execute());
    }

    public record CreateDebtorRequest(
        String name,
        String email
    ) {}
}
