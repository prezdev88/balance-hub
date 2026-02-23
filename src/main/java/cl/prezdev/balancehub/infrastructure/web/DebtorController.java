package cl.prezdev.balancehub.infrastructure.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.CreateDebtorInputPort;
import cl.prezdev.balancehub.application.ports.in.ListDebtorsInputPort;
import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorCommand;
import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorResult;
import cl.prezdev.balancehub.application.usecases.debtor.list.ListDebtorsResult;

@RestController
@RequestMapping("/api/debtors")
public class DebtorController {

    private final CreateDebtorInputPort createDebtorUseCase;
    private final ListDebtorsInputPort listDebtorsUseCase;

    public DebtorController(CreateDebtorInputPort createDebtorUseCase, ListDebtorsInputPort listDebtorsUseCase) {
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
