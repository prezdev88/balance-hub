package cl.prezdev.balancehub.infrastructure.security;

import cl.prezdev.balancehub.domain.enums.UserRole;

public record AuthenticatedUser(
    String userId,
    String email,
    UserRole role,
    String debtorId
) {
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isDebtor() {
        return role == UserRole.DEBTOR;
    }
}
