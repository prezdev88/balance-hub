package cl.prezdev.balancehub.infrastructure.config;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import cl.prezdev.balancehub.application.ports.in.CreateDebtInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateDebtorInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateRecurringExpenseInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateSalaryInputPort;
import cl.prezdev.balancehub.application.ports.in.DeleteFixedExpenseInputPort;
import cl.prezdev.balancehub.application.ports.in.GetDebtsInputPort;
import cl.prezdev.balancehub.application.ports.in.GetRecurringExpenseTotalInputPort;
import cl.prezdev.balancehub.application.ports.in.ListDebtorsInputPort;
import cl.prezdev.balancehub.application.ports.in.ListRecurringExpensesInputPort;
import cl.prezdev.balancehub.application.ports.in.PayInstallmentInputPort;
import cl.prezdev.balancehub.application.ports.in.UpdateFixedExpenseInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.application.ports.out.SalaryRepository;
import cl.prezdev.balancehub.application.usecases.debt.create.CreateDebtUseCase;
import cl.prezdev.balancehub.application.usecases.debt.get.GetDebtsUseCase;
import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorUseCase;
import cl.prezdev.balancehub.application.usecases.debtor.list.ListDebtorsUseCase;
import cl.prezdev.balancehub.application.usecases.installment.pay.PayInstallmentUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.create.CreateRecurringExpenseUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.delete.DeleteFixedExpenseUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseUseCase;
import cl.prezdev.balancehub.application.usecases.salary.create.CreateSalaryUseCase;

@Configuration
public class UseCaseConfiguration {

    @Bean
    CreateDebtorInputPort createDebtorUseCase(
        DebtorRepository debtorRepository,
        PlatformTransactionManager transactionManager
    ) {
        CreateDebtorUseCase delegate = new CreateDebtorUseCase(debtorRepository);
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> Objects.requireNonNull(tx.execute(status -> delegate.execute(command)));
    }

    @Bean
    ListDebtorsInputPort listDebtorsUseCase(DebtorRepository debtorRepository, DebtRepository debtRepository) {
        return new ListDebtorsUseCase(debtorRepository, debtRepository);
    }

    @Bean
    CreateDebtInputPort createDebtUseCase(
        DebtRepository debtRepository,
        InstallmentRepository installmentRepository,
        PlatformTransactionManager transactionManager
    ) {
        CreateDebtUseCase delegate = new CreateDebtUseCase(debtRepository, installmentRepository);
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> Objects.requireNonNull(tx.execute(status -> delegate.execute(command)));
    }

    @Bean
    GetDebtsInputPort getDebtsUseCase(
        DebtRepository debtRepository,
        DebtorRepository debtorRepository,
        InstallmentRepository installmentRepository
    ) {
        return new GetDebtsUseCase(debtRepository, debtorRepository, installmentRepository);
    }

    @Bean
    PayInstallmentInputPort payInstallmentUseCase(
        InstallmentRepository installmentRepository,
        PlatformTransactionManager transactionManager
    ) {
        PayInstallmentUseCase delegate = new PayInstallmentUseCase(installmentRepository);
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> tx.executeWithoutResult(status -> delegate.execute(command));
    }

    @Bean
    CreateRecurringExpenseInputPort createRecurringExpenseUseCase(
        RecurringExpenseRepository recurringExpenseRepository,
        PlatformTransactionManager transactionManager
    ) {
        CreateRecurringExpenseUseCase delegate = new CreateRecurringExpenseUseCase(recurringExpenseRepository);
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> Objects.requireNonNull(tx.execute(status -> delegate.execute(command)));
    }

    @Bean
    ListRecurringExpensesInputPort listRecurringExpensesUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new ListRecurringExpensesUseCase(recurringExpenseRepository);
    }

    @Bean
    UpdateFixedExpenseInputPort updateFixedExpenseUseCase(
        RecurringExpenseRepository recurringExpenseRepository,
        PlatformTransactionManager transactionManager
    ) {
        UpdateFixedExpenseUseCase delegate = new UpdateFixedExpenseUseCase(recurringExpenseRepository);
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> Objects.requireNonNull(tx.execute(status -> delegate.execute(command)));
    }

    @Bean
    DeleteFixedExpenseInputPort deleteFixedExpenseUseCase(
        RecurringExpenseRepository recurringExpenseRepository,
        PlatformTransactionManager transactionManager
    ) {
        DeleteFixedExpenseUseCase delegate = new DeleteFixedExpenseUseCase(recurringExpenseRepository);
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> tx.executeWithoutResult(status -> delegate.execute(command));
    }

    @Bean
    GetRecurringExpenseTotalInputPort getRecurringExpenseTotalUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new GetRecurringExpenseTotalUseCase(recurringExpenseRepository);
    }

    @Bean
    CreateSalaryInputPort createSalaryUseCase(
        SalaryRepository salaryRepository,
        PlatformTransactionManager transactionManager
    ) {
        CreateSalaryUseCase delegate = new CreateSalaryUseCase(salaryRepository);
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> Objects.requireNonNull(tx.execute(status -> delegate.execute(command)));
    }
}
