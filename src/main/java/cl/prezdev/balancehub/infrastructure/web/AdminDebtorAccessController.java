package cl.prezdev.balancehub.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.infrastructure.security.DebtorAccessResult;
import cl.prezdev.balancehub.infrastructure.security.DebtorAccessService;

@RestController
@RequestMapping("/api/admin/debtors")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminDebtorAccessController {

    private final DebtorAccessService debtorAccessService;

    public AdminDebtorAccessController(DebtorAccessService debtorAccessService) {
        this.debtorAccessService = debtorAccessService;
    }

    @PostMapping("/{debtorId}/access/grant")
    public ResponseEntity<DebtorAccessHttpResponse> grantAccess(
        @PathVariable String debtorId,
        @RequestBody(required = false) SetPasswordHttpRequest request
    ) {
        DebtorAccessResult result = debtorAccessService.grant(debtorId, request != null ? request.password() : null);
        return ResponseEntity.ok(toHttpResponse(result));
    }

    @PostMapping("/{debtorId}/access/password")
    public ResponseEntity<DebtorAccessHttpResponse> resetPassword(
        @PathVariable String debtorId,
        @RequestBody(required = false) SetPasswordHttpRequest request
    ) {
        DebtorAccessResult result = debtorAccessService.resetPassword(debtorId, request != null ? request.password() : null);
        return ResponseEntity.ok(toHttpResponse(result));
    }

    @PostMapping("/{debtorId}/access/revoke")
    public ResponseEntity<DebtorAccessHttpResponse> revokeAccess(@PathVariable String debtorId) {
        DebtorAccessResult result = debtorAccessService.revoke(debtorId);
        return ResponseEntity.ok(toHttpResponse(result));
    }

    private DebtorAccessHttpResponse toHttpResponse(DebtorAccessResult result) {
        return new DebtorAccessHttpResponse(
            result.debtorId(),
            result.email(),
            result.enabled(),
            result.password(),
            result.passwordGenerated()
        );
    }

    public record SetPasswordHttpRequest(String password) {}

    public record DebtorAccessHttpResponse(
        String debtorId,
        String email,
        boolean enabled,
        String password,
        boolean passwordGenerated
    ) {}
}
