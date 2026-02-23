package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.CreateRecurringExpenseInputPort;
import cl.prezdev.balancehub.application.ports.in.GetRecurringExpenseTotalInputPort;
import cl.prezdev.balancehub.application.ports.in.ListRecurringExpensesInputPort;
import cl.prezdev.balancehub.application.ports.in.UpdateFixedExpenseInputPort;
import cl.prezdev.balancehub.application.usecases.recurringexpense.create.CreateRecurringExpenseCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesResult;
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.RecurringExpenseListItem;
import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalResult;
import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseResult;
import cl.prezdev.balancehub.domain.enums.ExpenseType;

@RestController
@RequestMapping("/api/recurring-expenses")
public class RecurringExpenseController {

    private final CreateRecurringExpenseInputPort createUseCase;
    private final ListRecurringExpensesInputPort listUseCase;
    private final UpdateFixedExpenseInputPort updateFixedExpenseUseCase;
    private final GetRecurringExpenseTotalInputPort totalUseCase;

    public RecurringExpenseController(
        CreateRecurringExpenseInputPort createUseCase,
        ListRecurringExpensesInputPort listUseCase,
        UpdateFixedExpenseInputPort updateFixedExpenseUseCase,
        GetRecurringExpenseTotalInputPort totalUseCase
    ) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
        this.updateFixedExpenseUseCase = updateFixedExpenseUseCase;
        this.totalUseCase = totalUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateRecurringExpenseHttpResponse> create(@RequestBody CreateRecurringExpenseRequest request) {
        var result = createUseCase.execute(
            new CreateRecurringExpenseCommand(request.description(), request.amount(), request.type())
        );

        return ResponseEntity
            .created(URI.create("/api/recurring-expenses/" + result.recurringExpenseId()))
            .body(new CreateRecurringExpenseHttpResponse(result.recurringExpenseId()));
    }

    @GetMapping
    public ResponseEntity<ListRecurringExpensesHttpResponse> listByType(@RequestParam ExpenseType type) {
        ListRecurringExpensesResult result = listUseCase.execute(new ListRecurringExpensesCommand(type));
        return ResponseEntity.ok(
            new ListRecurringExpensesHttpResponse(
                result.recurringExpenses().stream().map(RecurringExpenseController::toHttpItem).toList()
            )
        );
    }

    @GetMapping("/total")
    public ResponseEntity<GetRecurringExpenseTotalHttpResponse> totalByType(@RequestParam ExpenseType type) {
        GetRecurringExpenseTotalResult result = totalUseCase.execute(new GetRecurringExpenseTotalCommand(type));
        return ResponseEntity.ok(new GetRecurringExpenseTotalHttpResponse(result.total()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateFixedExpenseHttpResponse> updateFixedExpense(
        @PathVariable String id,
        @RequestBody UpdateFixedExpenseRequest request
    ) {
        UpdateFixedExpenseResult result = updateFixedExpenseUseCase.execute(
            new UpdateFixedExpenseCommand(id, request.description(), request.amount())
        );
        return ResponseEntity.ok(
            new UpdateFixedExpenseHttpResponse(result.id(), result.description(), result.amount())
        );
    }

    private static RecurringExpenseHttpItem toHttpItem(RecurringExpenseListItem item) {
        return new RecurringExpenseHttpItem(item.id(), item.description(), item.amount());
    }

    public record CreateRecurringExpenseRequest(String description, BigDecimal amount, ExpenseType type) {}

    public record UpdateFixedExpenseRequest(String description, BigDecimal amount) {}

    public record CreateRecurringExpenseHttpResponse(String recurringExpenseId) {}

    public record ListRecurringExpensesHttpResponse(List<RecurringExpenseHttpItem> recurringExpenses) {}

    public record RecurringExpenseHttpItem(String id, String description, BigDecimal amount) {}

    public record GetRecurringExpenseTotalHttpResponse(BigDecimal total) {}

    public record UpdateFixedExpenseHttpResponse(String id, String description, BigDecimal amount) {}
}
