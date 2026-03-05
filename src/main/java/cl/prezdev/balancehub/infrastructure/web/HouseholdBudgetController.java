package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.ConfigureHouseholdBudgetInputPort;
import cl.prezdev.balancehub.application.ports.in.GetHouseholdBudgetSummaryInputPort;
import cl.prezdev.balancehub.application.ports.in.RegisterHouseholdExpenseInputPort;
import cl.prezdev.balancehub.application.ports.in.ResetHouseholdBudgetInputPort;
import cl.prezdev.balancehub.application.usecases.householdbudget.configure.ConfigureHouseholdBudgetCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.expense.RegisterHouseholdExpenseCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.reset.ResetHouseholdBudgetCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.summary.GetHouseholdBudgetSummaryCommand;
import cl.prezdev.balancehub.application.usecases.householdbudget.summary.HouseholdBudgetSummaryItem;
import cl.prezdev.balancehub.domain.enums.HouseholdBudgetCategory;

@RestController
@RequestMapping("/api/household-budgets")
@PreAuthorize("hasAuthority('ADMIN')")
public class HouseholdBudgetController {

    private final ConfigureHouseholdBudgetInputPort configureUseCase;
    private final RegisterHouseholdExpenseInputPort registerExpenseUseCase;
    private final ResetHouseholdBudgetInputPort resetUseCase;
    private final GetHouseholdBudgetSummaryInputPort summaryUseCase;

    public HouseholdBudgetController(
        ConfigureHouseholdBudgetInputPort configureUseCase,
        RegisterHouseholdExpenseInputPort registerExpenseUseCase,
        ResetHouseholdBudgetInputPort resetUseCase,
        GetHouseholdBudgetSummaryInputPort summaryUseCase
    ) {
        this.configureUseCase = configureUseCase;
        this.registerExpenseUseCase = registerExpenseUseCase;
        this.resetUseCase = resetUseCase;
        this.summaryUseCase = summaryUseCase;
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

    @PostMapping("/{category}/reset")
    public ResponseEntity<ResetHouseholdBudgetHttpResponse> reset(@PathVariable HouseholdBudgetCategory category) {
        var result = resetUseCase.execute(new ResetHouseholdBudgetCommand(category));
        return ResponseEntity.ok(new ResetHouseholdBudgetHttpResponse(
            result.category(),
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

    public record ConfigureHouseholdBudgetRequest(BigDecimal monthlyAmount) {}

    public record ConfigureHouseholdBudgetHttpResponse(
        HouseholdBudgetCategory category,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
    ) {}

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

    public record ResetHouseholdBudgetHttpResponse(
        HouseholdBudgetCategory category,
        BigDecimal monthlyAmount,
        BigDecimal remainingAmount,
        Instant updatedAt
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
