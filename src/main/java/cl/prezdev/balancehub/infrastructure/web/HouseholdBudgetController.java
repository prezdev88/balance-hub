package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.ConfigureHouseholdBudgetInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateHouseholdBagInputPort;
import cl.prezdev.balancehub.application.ports.in.GetHouseholdBudgetSummaryInputPort;
import cl.prezdev.balancehub.application.ports.in.GetHouseholdBagMovementHistoryInputPort;
import cl.prezdev.balancehub.application.ports.in.ListHouseholdBagsInputPort;
import cl.prezdev.balancehub.application.ports.in.RegisterHouseholdBagMovementInputPort;
import cl.prezdev.balancehub.application.ports.in.RegisterHouseholdExpenseInputPort;
import cl.prezdev.balancehub.application.ports.in.ResetHouseholdBagInputPort;
import cl.prezdev.balancehub.application.ports.in.ResetHouseholdBudgetInputPort;
import cl.prezdev.balancehub.application.ports.in.UpdateHouseholdBagBudgetInputPort;
import cl.prezdev.balancehub.application.usecases.householdbag.HouseholdBagDetails;
import cl.prezdev.balancehub.application.usecases.householdbag.create.CreateHouseholdBagCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.history.GetHouseholdBagMovementHistoryCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.history.HouseholdBagMovementHistoryItem;
import cl.prezdev.balancehub.application.usecases.householdbag.movement.RegisterHouseholdBagMovementCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.reset.ResetHouseholdBagCommand;
import cl.prezdev.balancehub.application.usecases.householdbag.updatebudget.UpdateHouseholdBagBudgetCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.configure.ConfigureHouseholdBudgetCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.expense.RegisterHouseholdExpenseCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.reset.ResetHouseholdBudgetCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.summary.GetHouseholdBudgetSummaryCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.summary.HouseholdBudgetSummaryItem;
import cl.prezdev.balancehub.domain.enums.HouseholdBagMovementType;
import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

@RestController
@RequestMapping("/api/household-budgets")
@PreAuthorize("hasAuthority('ADMIN')")
public class HouseholdBudgetController {

    private final ConfigureHouseholdBudgetInputPort configureUseCase;
    private final RegisterHouseholdExpenseInputPort registerExpenseUseCase;
    private final ResetHouseholdBudgetInputPort resetUseCase;
    private final GetHouseholdBudgetSummaryInputPort summaryUseCase;
    private final ListHouseholdBagsInputPort listBagsUseCase;
    private final CreateHouseholdBagInputPort createBagUseCase;
    private final UpdateHouseholdBagBudgetInputPort updateBudgetUseCase;
    private final RegisterHouseholdBagMovementInputPort registerMovementUseCase;
    private final ResetHouseholdBagInputPort resetBagUseCase;
    private final GetHouseholdBagMovementHistoryInputPort historyUseCase;

    public HouseholdBudgetController(
        ConfigureHouseholdBudgetInputPort configureUseCase,
        RegisterHouseholdExpenseInputPort registerExpenseUseCase,
        ResetHouseholdBudgetInputPort resetUseCase,
        GetHouseholdBudgetSummaryInputPort summaryUseCase,
        ListHouseholdBagsInputPort listBagsUseCase,
        CreateHouseholdBagInputPort createBagUseCase,
        UpdateHouseholdBagBudgetInputPort updateBudgetUseCase,
        RegisterHouseholdBagMovementInputPort registerMovementUseCase,
        ResetHouseholdBagInputPort resetBagUseCase,
        GetHouseholdBagMovementHistoryInputPort historyUseCase
    ) {
        this.configureUseCase = configureUseCase;
        this.registerExpenseUseCase = registerExpenseUseCase;
        this.resetUseCase = resetUseCase;
        this.summaryUseCase = summaryUseCase;
        this.listBagsUseCase = listBagsUseCase;
        this.createBagUseCase = createBagUseCase;
        this.updateBudgetUseCase = updateBudgetUseCase;
        this.registerMovementUseCase = registerMovementUseCase;
        this.resetBagUseCase = resetBagUseCase;
        this.historyUseCase = historyUseCase;
    }

    @GetMapping
    public ResponseEntity<ListHouseholdBagsHttpResponse> listBags() {
        var result = listBagsUseCase.execute(new cl.prezdev.balancehub.application.usecases.householdbag.list.ListHouseholdBagsCommand());
        List<HouseholdBagHttpResponse> bags = result.bags().stream()
            .map(HouseholdBudgetController::toBagHttpResponse)
            .toList();
        return ResponseEntity.ok(new ListHouseholdBagsHttpResponse(bags));
    }

    @PostMapping
    public ResponseEntity<CreateHouseholdBagHttpResponse> createBag(@RequestBody CreateHouseholdBagRequest request) {
        CreateHouseholdBagCommand command = new CreateHouseholdBagCommand(
            request.name(),
            request.emoji(),
            request.monthlyAmount()
        );
        var result = createBagUseCase.execute(command);
        HouseholdBagHttpResponse bag = toBagHttpResponse(result.bag());
        return ResponseEntity.ok(new CreateHouseholdBagHttpResponse(bag));
    }

    @PutMapping("/{category}")
    public ResponseEntity<ConfigureHouseholdBudgetHttpResponse> configure(
        @PathVariable HouseholdBudgetCategory category,
        @RequestBody ConfigureHouseholdBudgetRequest request
    ) {
        var result = configureUseCase.execute(new ConfigureHouseholdBudgetCommand(category, request.monthlyAmount()));
        return ResponseEntity.ok(new ConfigureHouseholdBudgetHttpResponse(
            result.category(),
            result.monthlyAmount(),
            result.remainingAmount(),
            result.updatedAt()
        ));
    }

    @PutMapping("/{bagId}/budget")
    public ResponseEntity<UpdateHouseholdBagBudgetHttpResponse> updateBudget(
        @PathVariable String bagId,
        @RequestBody UpdateHouseholdBagBudgetRequest request
    ) {
        UpdateHouseholdBagBudgetCommand command = new UpdateHouseholdBagBudgetCommand(bagId, request.monthlyAmount());
        var result = updateBudgetUseCase.execute(command);
        HouseholdBagHttpResponse bag = toBagHttpResponse(result.bag());
        return ResponseEntity.ok(new UpdateHouseholdBagBudgetHttpResponse(bag));
    }

    @PostMapping("/expenses")
    public ResponseEntity<RegisterHouseholdExpenseHttpResponse> registerExpense(@RequestBody RegisterHouseholdExpenseRequest request) {
        var result = registerExpenseUseCase.execute(
            new RegisterHouseholdExpenseCommand(request.category(), request.amount())
        );

        return ResponseEntity.ok(new RegisterHouseholdExpenseHttpResponse(
            result.category(),
            result.consumedAmount(),
            result.remainingAmount(),
            result.updatedAt()
        ));
    }

    @PostMapping("/{bagId}/movements")
    public ResponseEntity<RegisterHouseholdBagMovementHttpResponse> registerMovement(
        @PathVariable String bagId,
        @RequestBody RegisterHouseholdBagMovementRequest request
    ) {
        RegisterHouseholdBagMovementCommand command = new RegisterHouseholdBagMovementCommand(bagId, request.amount());
        var result = registerMovementUseCase.execute(command);
        return ResponseEntity.ok(new RegisterHouseholdBagMovementHttpResponse(
            result.bagId(),
            result.amount(),
            result.movementType(),
            result.remainingAmount(),
            result.updatedAt()
        ));
    }

    @GetMapping("/{bagId}/movements")
    public ResponseEntity<GetHouseholdBagMovementHistoryHttpResponse> getMovementHistory(@PathVariable String bagId) {
        GetHouseholdBagMovementHistoryCommand command = new GetHouseholdBagMovementHistoryCommand(bagId);
        var result = historyUseCase.execute(command);
        List<HouseholdBagMovementHistoryHttpItem> movements = result.movements().stream()
            .map(HouseholdBudgetController::toHistoryHttpItem)
            .toList();
        return ResponseEntity.ok(new GetHouseholdBagMovementHistoryHttpResponse(result.bagId(), movements));
    }

    @PostMapping("/{bagReference}/reset")
    public ResponseEntity<ResetHouseholdBagHttpResponse> reset(@PathVariable String bagReference) {
        ResetHouseholdBagCommand command = new ResetHouseholdBagCommand(bagReference);
        var result = resetBagUseCase.execute(command);
        return ResponseEntity.ok(new ResetHouseholdBagHttpResponse(
            result.bagId(),
            result.monthlyAmount(),
            result.remainingAmount(),
            result.updatedAt()
        ));
    }

    @GetMapping("/summary")
    public ResponseEntity<GetHouseholdBudgetSummaryHttpResponse> summary() {
        var result = summaryUseCase.execute(new GetHouseholdBudgetSummaryCommand());
        return ResponseEntity.ok(
            new GetHouseholdBudgetSummaryHttpResponse(
                result.budgets().stream().map(HouseholdBudgetController::toHttpItem).toList()
            )
        );
    }

    private static HouseholdBudgetSummaryHttpItem toHttpItem(HouseholdBudgetSummaryItem item) {
        return new HouseholdBudgetSummaryHttpItem(
            item.category(),
            item.monthlyAmount(),
            item.spentAmount(),
            item.remainingAmount()
        );
    }

    private static HouseholdBagHttpResponse toBagHttpResponse(HouseholdBagDetails bag) {
        return new HouseholdBagHttpResponse(
            bag.id(),
            bag.name(),
            bag.emoji(),
            bag.monthlyAmount(),
            bag.spentAmount(),
            bag.remainingAmount(),
            bag.updatedAt()
        );
    }

    private static HouseholdBagMovementHistoryHttpItem toHistoryHttpItem(HouseholdBagMovementHistoryItem item) {
        return new HouseholdBagMovementHistoryHttpItem(
            item.id(),
            item.amount(),
            item.type(),
            item.createdAt()
        );
    }

    public record ConfigureHouseholdBudgetRequest(BigDecimal monthlyAmount) {}

    public record CreateHouseholdBagRequest(
        String name,
        String emoji,
        BigDecimal monthlyAmount
    ) {}

    public record UpdateHouseholdBagBudgetRequest(BigDecimal monthlyAmount) {}

    public record RegisterHouseholdBagMovementRequest(BigDecimal amount) {}

    public record ConfigureHouseholdBudgetHttpResponse(
        HouseholdBudgetCategory category,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {}

    public record HouseholdBagHttpResponse(
        String id,
        String name,
        String emoji,
        BigDecimal monthlyAmount,
        BigDecimal spentAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {}

    public record ListHouseholdBagsHttpResponse(List<HouseholdBagHttpResponse> bags) {}

    public record CreateHouseholdBagHttpResponse(HouseholdBagHttpResponse bag) {}

    public record UpdateHouseholdBagBudgetHttpResponse(HouseholdBagHttpResponse bag) {}

    public record RegisterHouseholdExpenseRequest(
        HouseholdBudgetCategory category,
        BigDecimal amount
    ) {}

    public record RegisterHouseholdExpenseHttpResponse(
        HouseholdBudgetCategory category,
        BigDecimal consumedAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {}

    public record RegisterHouseholdBagMovementHttpResponse(
        String bagId,
        BigDecimal amount,
        HouseholdBagMovementType movementType,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {}

    public record ResetHouseholdBagHttpResponse(
        String bagId,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {}

    public record GetHouseholdBagMovementHistoryHttpResponse(
        String bagId,
        List<HouseholdBagMovementHistoryHttpItem> movements
    ) {}

    public record HouseholdBagMovementHistoryHttpItem(
        String id,
        BigDecimal amount,
        HouseholdBagMovementType type,
        Instant createdAt
    ) {}

    public record GetHouseholdBudgetSummaryHttpResponse(
        List<HouseholdBudgetSummaryHttpItem> budgets
    ) {}

    public record HouseholdBudgetSummaryHttpItem(
        HouseholdBudgetCategory category,
        BigDecimal monthlyAmount,
        BigDecimal spentAmount,
        BigDecimal remainingAmount
    ) {}
}
