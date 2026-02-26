package cl.prezdev.balancehub.application.usecases.debt.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.application.usecases.debt.create.command.CreateDebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.DebtCommand;
import cl.prezdev.balancehub.application.usecases.debt.create.command.InstallmentCommand;
import cl.prezdev.balancehub.domain.Debt;
import cl.prezdev.balancehub.domain.Installment;

class CreateDebtUseCaseTest {

    private CreateDebtUseCase useCase;
    private InMemoryDebtRepository debtRepo;
    private InMemoryInstallmentRepository installmentRepo;

    @BeforeEach
    void setUp() {
        debtRepo = new InMemoryDebtRepository();
        installmentRepo = new InMemoryInstallmentRepository();
        useCase = new CreateDebtUseCase(debtRepo, installmentRepo);
    }

    @Test
    void shouldThrowWhenRepositoriesAreNull() {
        assertThrows(IllegalArgumentException.class, () -> new CreateDebtUseCase(null, installmentRepo));
        assertThrows(IllegalArgumentException.class, () -> new CreateDebtUseCase(debtRepo, null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldCreateDebtAndInstallments() {
        var command = new CreateDebtCommand(
            new DebtCommand("debtor-1", "Phone", BigDecimal.valueOf(900)),
            new InstallmentCommand(3, BigDecimal.valueOf(300), LocalDate.of(2026, 3, 1))
        );

        var result = useCase.execute(command);

        assertNotNull(result);
        assertNotNull(result.debtId());
        assertEquals(1, debtRepo.saved.size());
        assertEquals(3, installmentRepo.saved.size());

        var savedDebt = debtRepo.saved.get(0);
        assertEquals(result.debtId(), savedDebt.getId());
        assertEquals("Phone", savedDebt.getDescription());
        assertEquals("debtor-1", savedDebt.getDebtorId());

        assertEquals(1, installmentRepo.saved.get(0).getNumber());
        assertEquals(2, installmentRepo.saved.get(1).getNumber());
        assertEquals(3, installmentRepo.saved.get(2).getNumber());
        assertEquals(LocalDate.of(2026, 3, 1), installmentRepo.saved.get(0).getDueDate());
        assertEquals(LocalDate.of(2026, 4, 1), installmentRepo.saved.get(1).getDueDate());
        assertEquals(LocalDate.of(2026, 5, 1), installmentRepo.saved.get(2).getDueDate());
        assertEquals(savedDebt.getId(), installmentRepo.saved.get(0).getDebtId());
    }

    static class InMemoryDebtRepository implements DebtRepository {
        List<Debt> saved = new ArrayList<>();

        @Override
        public void save(Debt debt) {
            saved.add(debt);
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
            return saved.stream()
                .filter(d -> d.getDebtorId().equals(debtorId))
                .filter(d -> {
                    LocalDate createdDate = d.getCreatedAt().atZone(java.time.ZoneOffset.UTC).toLocalDate();
                    return !(createdDate.isBefore(startDate) || createdDate.isAfter(endDate));
                })
                .toList();
        }
    }

    static class InMemoryInstallmentRepository implements InstallmentRepository {
        List<Installment> saved = new ArrayList<>();

        @Override
        public java.util.Optional<Installment> findById(String installmentId) {
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
