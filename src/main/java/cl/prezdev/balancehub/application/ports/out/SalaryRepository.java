package cl.prezdev.balancehub.application.ports.out;

import java.util.Optional;

import cl.prezdev.balancehub.domain.Salary;

public interface SalaryRepository {
    void save(Salary salary);

    void deactivateCurrentSalary();

    Optional<Salary> findActive();
}
