package cl.prezdev.balancehub.application.usecases.installment.unpaid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.exception.DebtorNotFoundException;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.domain.Debt;
import cl.prezdev.balancehub.domain.Debtor;
import cl.prezdev.balancehub.domain.Installment;

class GetUnpaidInstallmentsByMonthUseCaseTest {

    private GetUnpaidInstallmentsByMonthUseCase useCase;
    private InMemoryDebtorRepository debtorRepository;
    private InMemoryDebtRepository debtRepository;
    private InMemoryInstallmentRepository installmentRepository;

    @BeforeEach
    void setUp() {
        debtorRepository = new InMemoryDebtorRepository();
        debtRepository = new InMemoryDebtRepository();
        installmentRepository = new InMemoryInstallmentRepository();
        useCase = new GetUnpaidInstallmentsByMonthUseCase(debtorRepository, debtRepository, installmentRepository);
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldThrowWhenDebtorDoesNotExist() {
        var command = new GetUnpaidInstallmentsByMonthCommand("missing", 2026, 3);
        assertThrows(DebtorNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldReturnInstallmentsForRequestedMonthIncludingPaid() {
        Debtor debtor = new Debtor("debtor-1", "John", "john@doe.com");
        debtorRepository.saved.add(debtor);

        Debt debtA = new Debt("debt-a", "Laptop", BigDecimal.valueOf(1200), debtor.getId(), Instant.now(), false);
        Debt debtB = new Debt("debt-b", "Phone", BigDecimal.valueOf(900), debtor.getId(), Instant.now(), false);
        debtRepository.saved.add(debtA);
        debtRepository.saved.add(debtB);

        installmentRepository.saved.add(new Installment("i-1", debtA.getId(), 1, LocalDate.of(2026, 3, 5), BigDecimal.valueOf(400), null));
        installmentRepository.saved.add(new Installment("i-2", debtA.getId(), 2, LocalDate.of(2026, 3, 15), BigDecimal.valueOf(400), Instant.now()));
        installmentRepository.saved.add(new Installment("i-3", debtB.getId(), 1, LocalDate.of(2026, 3, 20), BigDecimal.valueOf(300), null));
        installmentRepository.saved.add(new Installment("i-4", debtB.getId(), 2, LocalDate.of(2026, 4, 20), BigDecimal.valueOf(300), null));

        var result = useCase.execute(new GetUnpaidInstallmentsByMonthCommand(debtor.getId(), 2026, 3));

        assertEquals(3, result.installments().size());
        assertEquals(0, result.totalAmount().compareTo(BigDecimal.valueOf(700)));
        assertEquals("Laptop", result.installments().get(0).debtDescription());
        assertEquals("Laptop", result.installments().get(1).debtDescription());
        assertEquals("Phone", result.installments().get(2).debtDescription());
        assertEquals(true, result.installments().get(1).paid());
    }

    static class InMemoryDebtorRepository implements DebtorRepository {
        List<Debtor> saved = new ArrayList<>();

        @Override
        public void save(Debtor debtor) {
            saved.add(debtor);
        }

        @Override
        public List<Debtor> findAll() {
            return List.copyOf(saved);
        }

        @Override
        public Optional<Debtor> findById(String id) {
            return saved.stream().filter(d -> d.getId().equals(id)).findFirst();
        }
    }

    static class InMemoryDebtRepository implements DebtRepository {
        List<Debt> saved = new ArrayList<>();

        @Override
        public void save(Debt debt) {
            saved.add(debt);
        }

        @Override
        public Optional<Debt> findById(String debtId) {
            return saved.stream().filter(d -> d.getId().equals(debtId)).findFirst();
        }

        @Override
        public void deleteById(String debtId) {
            saved.removeIf(d -> d.getId().equals(debtId));
        }

        @Override
        public BigDecimal totalByDebtorId(String debtorId) {
            return saved.stream()
                .filter(d -> d.getDebtorId().equals(debtorId))
                .map(Debt::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        @Override
        public List<Debt> findByDebtorIdAndDateRange(String debtorId, LocalDate startDate, LocalDate endDate) {
            return saved.stream().filter(d -> d.getDebtorId().equals(debtorId)).toList();
        }
    }

    static class InMemoryInstallmentRepository implements InstallmentRepository {
        List<Installment> saved = new ArrayList<>();

        @Override
        public Optional<Installment> findById(String installmentId) {
            return saved.stream().filter(i -> i.getId().equals(installmentId)).findFirst();
        }

        @Override
        public void save(List<Installment> installments) {
            saved.addAll(installments);
        }

        @Override
        public void save(Installment installment) {
            saved.add(installment);
        }

        @Override
        public List<Installment> findByDebtIds(List<String> debtIds) {
            return saved.stream().filter(i -> debtIds.contains(i.getDebtId())).toList();
        }
    }
}
