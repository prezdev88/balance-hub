package cl.prezdev.balancehub.application.usecases.installment.pay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.prezdev.balancehub.application.exception.InstallmentNotFoundException;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.domain.Installment;
import cl.prezdev.balancehub.domain.exception.InvalidInstallmentException;

class PayInstallmentUseCaseTest {

    private PayInstallmentUseCase useCase;
    private InMemoryInstallmentRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryInstallmentRepository();
        useCase = new PayInstallmentUseCase(repo);
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new PayInstallmentUseCase(null));
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldThrowWhenInstallmentIdIsBlank() {
        assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(new PayInstallmentCommand(" ", Instant.now())));
    }

    @Test
    void shouldThrowWhenInstallmentIdIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(new PayInstallmentCommand(null, Instant.now())));
    }

    @Test
    void shouldThrowWhenInstallmentDoesNotExist() {
        assertThrows(InstallmentNotFoundException.class,
            () -> useCase.execute(new PayInstallmentCommand("missing", Instant.now())));
    }

    @Test
    void shouldPayAndSaveInstallment() {
        var installment = new Installment("inst-1", "debt-1", 1, LocalDate.now(), BigDecimal.TEN, null);
        repo.saved.add(installment);
        var paymentDate = Instant.parse("2026-02-22T20:30:00Z");

        useCase.execute(new PayInstallmentCommand(installment.getId(), paymentDate));

        assertTrue(installment.isPaid());
        assertEquals(paymentDate, installment.getPaidAt());
        assertNotNull(repo.lastSaved);
        assertEquals(installment.getId(), repo.lastSaved.getId());
    }

    @Test
    void shouldPropagateDomainExceptionWhenAlreadyPaid() {
        var installment = new Installment(
            "inst-1", "debt-1", 1, LocalDate.now(), BigDecimal.TEN, Instant.now()
        );
        repo.saved.add(installment);

        assertThrows(InvalidInstallmentException.class,
            () -> useCase.execute(new PayInstallmentCommand(installment.getId(), Instant.now())));
    }

    @Test
    void shouldPropagateDomainExceptionWhenPaymentDateIsNullAndNotSave() {
        var installment = new Installment("inst-1", "debt-1", 1, LocalDate.now(), BigDecimal.TEN, null);
        repo.saved.add(installment);

        assertThrows(InvalidInstallmentException.class,
            () -> useCase.execute(new PayInstallmentCommand(installment.getId(), null)));
        assertEquals(null, repo.lastSaved);
    }

    static class InMemoryInstallmentRepository implements InstallmentRepository {
        List<Installment> saved = new ArrayList<>();
        Installment lastSaved;

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
            this.lastSaved = installment;
        }

        @Override
        public List<Installment> findByDebtIds(List<String> debtIds) {
            return saved.stream().filter(i -> debtIds.contains(i.getDebtId())).toList();
        }
    }
}
