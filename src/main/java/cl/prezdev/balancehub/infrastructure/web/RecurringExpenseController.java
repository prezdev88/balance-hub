package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.usecases.recurringexpense.create.CreateRecurringExpenseCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.create.CreateRecurringExpenseResult;
import cl.prezdev.balancehub.application.usecases.recurringexpense.create.CreateRecurringExpenseUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesResult;
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalResult;
import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseCommand;
import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseResult;
import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseUseCase;
import cl.prezdev.balancehub.domain.enums.ExpenseType;

@RestController
@RequestMapping("/api/recurring-expenses")
public class RecurringExpenseController {

    private final CreateRecurringExpenseUseCase createUseCase;
    private final ListRecurringExpensesUseCase listUseCase;
    private final UpdateFixedExpenseUseCase updateFixedExpenseUseCase;
    private final GetRecurringExpenseTotalUseCase totalUseCase;

    public RecurringExpenseController(
        CreateRecurringExpenseUseCase createUseCase,
        ListRecurringExpensesUseCase listUseCase,
        UpdateFixedExpenseUseCase updateFixedExpenseUseCase,
        GetRecurringExpenseTotalUseCase totalUseCase
    ) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
        this.updateFixedExpenseUseCase = updateFixedExpenseUseCase;
        this.totalUseCase = totalUseCase;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CreateRecurringExpenseResult> create(@RequestBody CreateRecurringExpenseRequest request) {
        var result = createUseCase.execute(
            new CreateRecurringExpenseCommand(request.description(), request.amount(), request.type())
        );

        return ResponseEntity
            .created(URI.create("/api/recurring-expenses/" + result.recurringExpenseId()))
            .body(result);
    }

    @GetMapping
    public ResponseEntity<ListRecurringExpensesResult> listByType(@RequestParam ExpenseType type) {
        return ResponseEntity.ok(listUseCase.execute(new ListRecurringExpensesCommand(type)));
    }

    @GetMapping("/total")
    public ResponseEntity<GetRecurringExpenseTotalResult> totalByType(@RequestParam ExpenseType type) {
        return ResponseEntity.ok(totalUseCase.execute(new GetRecurringExpenseTotalCommand(type)));
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<UpdateFixedExpenseResult> updateFixedExpense(
        @PathVariable String id,
        @RequestBody UpdateFixedExpenseRequest request
    ) {
        return ResponseEntity.ok(
            updateFixedExpenseUseCase.execute(
                new UpdateFixedExpenseCommand(id, request.description(), request.amount())
            )
        );
    }

    public record CreateRecurringExpenseRequest(String description, BigDecimal amount, ExpenseType type) {}

    public record UpdateFixedExpenseRequest(String description, BigDecimal amount) {}
}
