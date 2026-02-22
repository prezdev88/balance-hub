package cl.prezdev.balancehub.infrastructure.persistence.jpa.adapter;

import org.springframework.stereotype.Repository;

import cl.prezdev.balancehub.application.ports.out.SalaryRepository;
import cl.prezdev.balancehub.domain.Salary;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.entity.SalaryJpaEntity;
import cl.prezdev.balancehub.infrastructure.persistence.jpa.repository.SalaryJpaRepository;

@Repository
public class SalaryJpaAdapter implements SalaryRepository {

    private final SalaryJpaRepository repository;

    public SalaryJpaAdapter(SalaryJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Salary salary) {
        repository.save(new SalaryJpaEntity(
            salary.getId(),
            salary.getAmount(),
            salary.getCreatedAt(),
            salary.isActive()
        ));
    }

    @Override
    public void deactivateCurrentSalary() {
        repository.deactivateCurrentSalary();
    }
}
