package cl.prezdev.balancehub.infrastructure.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

public final class SecurityAccess {

    private SecurityAccess() {
    }

    public static AuthenticatedUser requireUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser principal)) {
            throw new AccessDeniedException("Unauthorized");
        }
        return principal;
    }

    public static void requireAdminOrDebtorOwner(Authentication authentication, String debtorId) {
        AuthenticatedUser principal = requireUser(authentication);
        if (principal.isAdmin()) {
            return;
        }

        if (principal.isDebtor() && principal.debtorId() != null && principal.debtorId().equals(debtorId)) {
            return;
        }

        throw new AccessDeniedException("Forbidden");
    }
}
