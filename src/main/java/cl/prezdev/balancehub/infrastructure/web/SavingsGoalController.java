package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.CreateSavingsGoalInputPort;
import cl.prezdev.balancehub.application.usecases.savingsgoal.create.CreateSavingsGoalCommand;

@RestController
@RequestMapping("/api/savings-goals")
public class SavingsGoalController {

    private final CreateSavingsGoalInputPort createSavingsGoalUseCase;

    public SavingsGoalController(CreateSavingsGoalInputPort createSavingsGoalUseCase) {
        this.createSavingsGoalUseCase = createSavingsGoalUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateSavingsGoalHttpResponse> create(@RequestBody CreateSavingsGoalRequest request) {
        var result = createSavingsGoalUseCase.execute(new CreateSavingsGoalCommand(request.amount()));

        return ResponseEntity
            .created(URI.create("/api/savings-goals/" + result.id()))
            .body(new CreateSavingsGoalHttpResponse(result.id(), result.amount(), result.createdAt()));
    }

    public record CreateSavingsGoalRequest(BigDecimal amount) {}

    public record CreateSavingsGoalHttpResponse(String id, BigDecimal amount, Instant createdAt) {}
}
