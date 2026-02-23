package cl.prezdev.balancehub.application.usecases.salary.create;

import cl.prezdev.balancehub.application.ports.in.CreateSalaryInputPort;
import cl.prezdev.balancehub.application.ports.out.SalaryRepository;
import cl.prezdev.balancehub.domain.Salary;

public class CreateSalaryUseCase implements CreateSalaryInputPort {

    private final SalaryRepository repository;

    public CreateSalaryUseCase(SalaryRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("SalaryRepository cannot be null");
        }

        this.repository = repository;
    }

    @Override
    public CreateSalaryResult execute(CreateSalaryCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }

        var salary = new Salary(command.amount());

        repository.deactivateCurrentSalary();
        repository.save(salary);

        return new CreateSalaryResult(
            salary.getId(),
            salary.getAmount(),
            salary.getCreatedAt()
        );
    }
}
