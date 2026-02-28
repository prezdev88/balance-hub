package cl.prezdev.balancehub.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.infrastructure.security.AuthLoginResult;
import cl.prezdev.balancehub.infrastructure.security.AuthService;
import cl.prezdev.balancehub.infrastructure.security.AuthenticatedUser;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginHttpResponse> login(@RequestBody LoginHttpRequest request) {
        AuthLoginResult result = authService.login(request.email(), request.password());
        return ResponseEntity.ok(new LoginHttpResponse(
            result.accessToken(),
            result.tokenType(),
            result.expiresAt(),
            result.userId(),
            result.email(),
            result.role(),
            result.debtorId(),
            result.mustChangePassword()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        authService.logout(extractBearerToken(authHeader));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
        @AuthenticationPrincipal AuthenticatedUser user,
        @RequestBody ChangePasswordHttpRequest request
    ) {
        authService.changePassword(user.userId(), request != null ? request.newPassword() : null);
        return ResponseEntity.noContent().build();
    }

    private String extractBearerToken(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }
        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7).trim();
        return token.isBlank() ? null : token;
    }

    public record LoginHttpRequest(String email, String password) {}

    public record ChangePasswordHttpRequest(String newPassword) {}

    public record LoginHttpResponse(
        String accessToken,
        String tokenType,
        java.time.Instant expiresAt,
        String userId,
        String email,
        cl.prezdev.balancehub.domain.enums.UserRole role,
        String debtorId,
        boolean mustChangePassword
    ) {}
}
