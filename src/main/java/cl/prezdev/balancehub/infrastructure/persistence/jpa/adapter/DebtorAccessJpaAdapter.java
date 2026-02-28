package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import org.springframework.stereotype.Component;

import cl.prezdev.balancehub.application.ports.out.DebtorAccessRepository;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.AuthUserJpaRepository;

@Component
public class DebtorAccessJpaAdapter implements DebtorAccessRepository {

    private final AuthUserJpaRepository authUserJpaRepository;

    public DebtorAccessJpaAdapter(AuthUserJpaRepository authUserJpaRepository) {
        this.authUserJpaRepository = authUserJpaRepository;
    }

    @Override
    public boolean canLogin(String debtorId) {
        return authUserJpaRepository.existsByDebtorIdAndEnabledTrue(debtorId);
    }
}
