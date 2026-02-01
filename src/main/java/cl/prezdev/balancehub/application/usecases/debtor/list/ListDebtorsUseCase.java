package cl.prezdev.balancehub.application.usecases.debtor.list;

import java.util.List;

import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.domain.Debtor;

public class ListDebtorsUseCase {

    private final DebtorRepository repository;

    public ListDebtorsUseCase(DebtorRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");  
        }

        this.repository = repository;
    }

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
                debtor.getEmail()
            ))
            .toList();
    }
}
