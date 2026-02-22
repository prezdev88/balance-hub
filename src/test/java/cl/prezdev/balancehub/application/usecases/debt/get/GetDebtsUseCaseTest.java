package cl.prezdev.balancehub.application.usecases.debt.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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

class GetDebtsUseCaseTest {

    private GetDebtsUseCase useCase;
    private InMemoryDebtRepository debtRepo;
    private InMemoryDebtorRepository debtorRepo;
    private InMemoryInstallmentRepository installmentRepo;

    @BeforeEach
    void setUp() {
        debtRepo = new InMemoryDebtRepository();
        debtorRepo = new InMemoryDebtorRepository();
        installmentRepo = new InMemoryInstallmentRepository();
        useCase = new GetDebtsUseCase(debtRepo, debtorRepo, installmentRepo);
    }

    @Test
    void shouldThrowWhenDebtorDoesNotExist() {
        var command = new GetDebtsCommand("missing", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));

        assertThrows(DebtorNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldReturnDebtorAndDebtsWithSortedInstallments() {
        var debtor = new Debtor("debtor-1", "John", "john@doe.com");
        debtorRepo.saved.add(debtor);

        var debt = new Debt(
            "debt-1",
            "Laptop",
            BigDecimal.valueOf(1200),
            debtor.getId(),
            Instant.parse("2026-02-10T10:00:00Z"),
            false
        );
        debtRepo.saved.add(debt);

        installmentRepo.saved.add(new Installment(
            "inst-2", debt.getId(), 2, LocalDate.of(2026, 4, 10), BigDecimal.valueOf(600), null
        ));
        installmentRepo.saved.add(new Installment(
            "inst-1", debt.getId(), 1, LocalDate.of(2026, 3, 10), BigDecimal.valueOf(600), null
        ));

        var command = new GetDebtsCommand(debtor.getId(), LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
        var result = useCase.execute(command);

        assertNotNull(result);
        assertEquals(debtor.getId(), result.id());
        assertEquals("John", result.name());
        assertEquals(1, result.debts().size());
        assertEquals("debt-1", result.debts().get(0).id());
        assertEquals(2, result.debts().get(0).installments().size());
        assertEquals(1, result.debts().get(0).installments().get(0).number());
        assertEquals(2, result.debts().get(0).installments().get(1).number());
    }

    @Test
    void shouldReturnEmptyDebtsWhenNoDebtInRange() {
        var debtor = new Debtor("debtor-1", "Jane", "jane@doe.com");
        debtorRepo.saved.add(debtor);

        debtRepo.saved.add(new Debt(
            "debt-1",
            "Old debt",
            BigDecimal.TEN,
            debtor.getId(),
            Instant.parse("2025-01-01T00:00:00Z"),
            false
        ));

        var result = useCase.execute(new GetDebtsCommand(
            debtor.getId(),
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        ));

        assertNotNull(result);
        assertEquals(0, result.debts().size());
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
        public List<Debt> findByDebtorIdAndDateRange(String debtorId, LocalDate startDate, LocalDate endDate) {
            return saved.stream()
                .filter(d -> d.getDebtorId().equals(debtorId))
                .filter(d -> {
                    LocalDate date = d.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDate();
                    return !date.isBefore(startDate) && !date.isAfter(endDate);
                })
                .toList();
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
