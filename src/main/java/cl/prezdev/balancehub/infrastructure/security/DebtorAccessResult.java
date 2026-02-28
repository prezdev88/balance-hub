package cl.prezdev.balancehub.infrastructure.security;

public record DebtorAccessResult(
    String debtorId,
    String email,
    boolean enabled,
    String password,
    boolean passwordGenerated
) {}
