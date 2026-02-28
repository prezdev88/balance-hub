package cl.prezdev.balancehub.application.usecases.debtor.list;

import java.util.List;

import cl.prezdev.balancehub.application.ports.in.ListDebtorsInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtorAccessRepository;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.domain.Debtor;

public class ListDebtorsUseCase implements ListDebtorsInputPort {

    private final DebtorRepository repository;
    private final DebtRepository debtRepository;
    private final DebtorAccessRepository debtorAccessRepository;

    public ListDebtorsUseCase(
        DebtorRepository repository,
        DebtRepository debtRepository,
        DebtorAccessRepository debtorAccessRepository
    ) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }
        if (debtRepository == null) {
            throw new IllegalArgumentException("debtRepository must not be null");
        }
        if (debtorAccessRepository == null) {
            throw new IllegalArgumentException("debtorAccessRepository must not be null");
        }

        this.repository = repository;
        this.debtRepository = debtRepository;
        this.debtorAccessRepository = debtorAccessRepository;
    }

    @Override
    public ListDebtorsResult execute() {
        var debtors = repository.findAll();
        var debtorsInfo = map(debtors);

        return new ListDebtorsResult(debtorsInfo);
    }

    private List<DebtorListItem> map(List<Debtor> debtors) {
        return debtors.stream()
            .map(debtor -> new DebtorListItem(
                debtor.getId(),
                debtor.getName(),
                debtor.getEmail(),
                debtRepository.totalByDebtorId(debtor.getId()),
                debtorAccessRepository.canLogin(debtor.getId())
            ))
            .toList();
    }
}
