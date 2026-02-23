package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.CreateSalaryInputPort;
import cl.prezdev.balancehub.application.usecases.salary.create.CreateSalaryCommand;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {

    private final CreateSalaryInputPort createSalaryUseCase;

    public SalaryController(CreateSalaryInputPort createSalaryUseCase) {
        this.createSalaryUseCase = createSalaryUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateSalaryHttpResponse> create(@RequestBody CreateSalaryRequest request) {
        var result = createSalaryUseCase.execute(new CreateSalaryCommand(request.amount()));

        return ResponseEntity
            .created(URI.create("/api/salaries/" + result.id()))
            .body(new CreateSalaryHttpResponse(result.id(), result.amount(), result.createdAt()));
    }

    public record CreateSalaryRequest(BigDecimal amount) {}

    public record CreateSalaryHttpResponse(String id, BigDecimal amount, Instant createdAt) {}
}
