package cl.prezdev.balancehub.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.infrastructure.security.AuthLoginResult;
import cl.prezdev.balancehub.infrastructure.security.AuthService;

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
            result.debtorId()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        authService.logout(extractBearerToken(authHeader));
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

    public record LoginHttpResponse(
        String accessToken,
        String tokenType,
        java.time.Instant expiresAt,
        String userId,
        String email,
        cl.prezdev.balancehub.domain.enums.UserRole role,
        String debtorId
    ) {}
}
