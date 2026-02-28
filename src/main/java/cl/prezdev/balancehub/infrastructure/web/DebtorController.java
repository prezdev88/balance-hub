package cl.prezdev.balancehub.infrastructure.web;

import java.net.URI;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.CreateDebtorInputPort;
import cl.prezdev.balancehub.application.ports.in.ListDebtorsInputPort;
import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorCommand;
import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorResult;
import cl.prezdev.balancehub.application.usecases.debtor.list.DebtorListItem;
import cl.prezdev.balancehub.application.usecases.debtor.list.ListDebtorsResult;

@RestController
@RequestMapping("/api/debtors")
@PreAuthorize("hasAuthority('ADMIN')")
public class DebtorController {

    private final CreateDebtorInputPort createDebtorUseCase;
    private final ListDebtorsInputPort listDebtorsUseCase;

    public DebtorController(CreateDebtorInputPort createDebtorUseCase, ListDebtorsInputPort listDebtorsUseCase) {
        this.createDebtorUseCase = createDebtorUseCase;
        this.listDebtorsUseCase = listDebtorsUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateDebtorHttpResponse> create(@RequestBody CreateDebtorRequest request) {
        CreateDebtorResult result = createDebtorUseCase.execute(
            new CreateDebtorCommand(request.name(), request.email())
        );

        return ResponseEntity
            .created(URI.create("/api/debtors/" + result.debtorId()))
            .body(new CreateDebtorHttpResponse(result.debtorId()));
    }

    @GetMapping
    public ResponseEntity<ListDebtorsHttpResponse> list() {
        ListDebtorsResult result = listDebtorsUseCase.execute();
        return ResponseEntity.ok(
            new ListDebtorsHttpResponse(
                result.debtors().stream().map(DebtorController::toHttpItem).toList()
            )
        );
    }

    private static DebtorHttpItem toHttpItem(DebtorListItem item) {
        return new DebtorHttpItem(item.id(), item.name(), item.email(), item.totalDebt(), item.accessEnabled());
    }

    public record CreateDebtorRequest(
        String name,
        String email
    ) {}

    public record CreateDebtorHttpResponse(String debtorId) {}

    public record ListDebtorsHttpResponse(List<DebtorHttpItem> debtors) {}

    public record DebtorHttpItem(String id, String name, String email, BigDecimal totalDebt, boolean accessEnabled) {}
}
