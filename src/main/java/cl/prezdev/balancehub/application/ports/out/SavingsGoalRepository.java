package cl.prezdev.balancehub.application.ports.out;

import java.util.Optional;

import cl.prezdev.balancehub.domain.SavingsGoal;

public interface SavingsGoalRepository {
    void save(SavingsGoal savingsGoal);

    void deactivateCurrentSavingsGoal();

    Optional<SavingsGoal> findActive();
}
