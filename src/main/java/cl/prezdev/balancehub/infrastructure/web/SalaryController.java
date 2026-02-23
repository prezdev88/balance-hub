package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.CreateSalaryInputPort;
import cl.prezdev.balancehub.application.usecases.salary.create.CreateSalaryCommand;
import cl.prezdev.balancehub.application.usecases.salary.create.CreateSalaryResult;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {

    private final CreateSalaryInputPort createSalaryUseCase;

    public SalaryController(CreateSalaryInputPort createSalaryUseCase) {
        this.createSalaryUseCase = createSalaryUseCase;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CreateSalaryResult> create(@RequestBody CreateSalaryRequest request) {
        var result = createSalaryUseCase.execute(new CreateSalaryCommand(request.amount()));

        return ResponseEntity
            .created(URI.create("/api/salaries/" + result.id()))
            .body(result);
    }

    public record CreateSalaryRequest(BigDecimal amount) {}
}
