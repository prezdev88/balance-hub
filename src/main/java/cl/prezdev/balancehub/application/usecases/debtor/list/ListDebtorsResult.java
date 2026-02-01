package cl.prezdev.balancehub.application.usecases.debtor.list;

import java.util.List;

public class ListDebtorsResult {

    private List<DebtorListItem> debtors;

    public ListDebtorsResult(List<DebtorListItem> debtors) {
        this.debtors = debtors;
    }

    public List<DebtorListItem> getDebtors() {
        return debtors;
    }

    public void setDebtors(List<DebtorListItem> debtors) {
        this.debtors = debtors;
    }
}