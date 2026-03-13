package cl.prezdev.balancehub.infrastructure.web;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.CreatePendingInputPort;
import cl.prezdev.balancehub.application.ports.in.DeletePendingInputPort;
import cl.prezdev.balancehub.application.ports.in.ListPendingsInputPort;
import cl.prezdev.balancehub.application.usecases.pending.create.CreatePendingCommand;
import cl.prezdev.balancehub.application.usecases.pending.delete.DeletePendingCommand;
import cl.prezdev.balancehub.application.usecases.pending.list.ListPendingsCommand;
import cl.prezdev.balancehub.application.usecases.pending.list.PendingListItem;

@RestController
@RequestMapping("/api/pendings")
@PreAuthorize("hasAuthority('ADMIN')")
public class PendingController {

    private final CreatePendingInputPort createUseCase;
    private final ListPendingsInputPort listUseCase;
    private final DeletePendingInputPort deleteUseCase;

    public PendingController(
        CreatePendingInputPort createUseCase,
        ListPendingsInputPort listUseCase,
        DeletePendingInputPort deleteUseCase
    ) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @PostMapping
    public ResponseEntity<CreatePendingHttpResponse> create(@RequestBody CreatePendingRequest request) {
        var result = createUseCase.execute(new CreatePendingCommand(request.description()));
        return ResponseEntity
            .created(URI.create("/api/pendings/" + result.pendingId()))
            .body(new CreatePendingHttpResponse(result.pendingId(), result.description(), result.createdAt()));
    }

    @GetMapping
    public ResponseEntity<ListPendingsHttpResponse> list() {
        var result = listUseCase.execute(new ListPendingsCommand());
        return ResponseEntity.ok(
            new ListPendingsHttpResponse(result.pendings().stream().map(PendingController::toHttpItem).toList())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteUseCase.execute(new DeletePendingCommand(id));
        return ResponseEntity.noContent().build();
    }

    private static PendingHttpItem toHttpItem(PendingListItem item) {
        return new PendingHttpItem(item.id(), item.description(), item.createdAt());
    }

    public record CreatePendingRequest(String description) {}

    public record CreatePendingHttpResponse(String pendingId, String description, Instant createdAt) {}

    public record ListPendingsHttpResponse(List<PendingHttpItem> pendings) {}

    public record PendingHttpItem(String id, String description, Instant createdAt) {}
}
