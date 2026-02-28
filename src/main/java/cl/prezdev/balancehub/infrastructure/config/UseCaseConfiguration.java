package cl.prezdev.balancehub.infrastructure.config;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import cl.prezdev.balancehub.application.ports.in.CreateDebtInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateDebtorInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateRecurringExpenseInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateSavingsGoalInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateSalaryInputPort;
import cl.prezdev.balancehub.application.ports.in.DeleteDebtInputPort;
import cl.prezdev.balancehub.application.ports.in.DeleteFixedExpenseInputPort;
import cl.prezdev.balancehub.application.ports.in.GetDebtDetailInputPort;
import cl.prezdev.balancehub.application.ports.in.GetMonthlySalarySnapshotInputPort;
import cl.prezdev.balancehub.application.ports.in.GetMonthlyFreeAmountInputPort;
import cl.prezdev.balancehub.application.ports.in.GetMonthlySummaryReportInputPort;
import cl.prezdev.balancehub.application.ports.in.GetRecurringExpenseTotalInputPort;
import cl.prezdev.balancehub.application.ports.in.GetUnpaidInstallmentsByMonthInputPort;
import cl.prezdev.balancehub.application.ports.in.ListDebtorsInputPort;
import cl.prezdev.balancehub.application.ports.in.ListRecurringExpensesInputPort;
import cl.prezdev.balancehub.application.ports.in.PayInstallmentInputPort;
import cl.prezdev.balancehub.application.ports.in.PayMonthlySalaryInputPort;
import cl.prezdev.balancehub.application.ports.in.UpdateFixedExpenseInputPort;
import cl.prezdev.balancehub.application.ports.out.DebtorRepository;
import cl.prezdev.balancehub.application.ports.out.DebtorAccessRepository;
import cl.prezdev.balancehub.application.ports.out.DebtRepository;
import cl.prezdev.balancehub.application.ports.out.InstallmentRepository;
import cl.prezdev.balancehub.application.ports.out.MonthlySalarySnapshotRepository;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.application.ports.out.SavingsGoalRepository;
import cl.prezdev.balancehub.application.ports.out.SalaryRepository;
import cl.prezdev.balancehub.application.usecases.debt.create.CreateDebtUseCase;
import cl.prezdev.balancehub.application.usecases.debt.delete.DeleteDebtUseCase;
import cl.prezdev.balancehub.application.usecases.debt.getdetail.GetDebtDetailUseCase;
import cl.prezdev.balancehub.application.usecases.debtor.create.CreateDebtorUseCase;
import cl.prezdev.balancehub.application.usecases.debtor.list.ListDebtorsUseCase;
import cl.prezdev.balancehub.application.usecases.financialplan.GetMonthlyFreeAmountUseCase;
import cl.prezdev.balancehub.application.usecases.installment.pay.PayInstallmentUseCase;
import cl.prezdev.balancehub.application.usecases.installment.unpaid.GetUnpaidInstallmentsByMonthUseCase;
import cl.prezdev.balancehub.application.usecases.report.monthly.GetMonthlySummaryReportUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.create.CreateRecurringExpenseUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.delete.DeleteFixedExpenseUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseUseCase;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.get.GetMonthlySalarySnapshotUseCase;
import cl.prezdev.balancehub.application.usecases.salarysnapshot.pay.PayMonthlySalaryUseCase;
import cl.prezdev.balancehub.application.usecases.savingsgoal.create.CreateSavingsGoalUseCase;
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
    ListDebtorsInputPort listDebtorsUseCase(
        DebtorRepository debtorRepository,
        DebtRepository debtRepository,
        DebtorAccessRepository debtorAccessRepository
    ) {
        return new ListDebtorsUseCase(debtorRepository, debtRepository, debtorAccessRepository);
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
    DeleteDebtInputPort deleteDebtUseCase(
        DebtRepository debtRepository,
        PlatformTransactionManager transactionManager
    ) {
        DeleteDebtUseCase delegate = new DeleteDebtUseCase(debtRepository);
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> tx.executeWithoutResult(status -> delegate.execute(command));
    }

    @Bean
    GetDebtDetailInputPort getDebtDetailUseCase(
        DebtRepository debtRepository,
        DebtorRepository debtorRepository,
        InstallmentRepository installmentRepository
    ) {
        return new GetDebtDetailUseCase(debtRepository, debtorRepository, installmentRepository);
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
    GetUnpaidInstallmentsByMonthInputPort getUnpaidInstallmentsByMonthUseCase(
        DebtorRepository debtorRepository,
        DebtRepository debtRepository,
        InstallmentRepository installmentRepository
    ) {
        return new GetUnpaidInstallmentsByMonthUseCase(debtorRepository, debtRepository, installmentRepository);
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

    @Bean
    CreateSavingsGoalInputPort createSavingsGoalUseCase(
        SavingsGoalRepository savingsGoalRepository,
        PlatformTransactionManager transactionManager
    ) {
        CreateSavingsGoalUseCase delegate = new CreateSavingsGoalUseCase(savingsGoalRepository);
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> Objects.requireNonNull(tx.execute(status -> delegate.execute(command)));
    }

    @Bean
    GetMonthlyFreeAmountInputPort getMonthlyFreeAmountUseCase(
        SalaryRepository salaryRepository,
        SavingsGoalRepository savingsGoalRepository,
        RecurringExpenseRepository recurringExpenseRepository
    ) {
        return new GetMonthlyFreeAmountUseCase(salaryRepository, savingsGoalRepository, recurringExpenseRepository);
    }

    @Bean
    GetMonthlySalarySnapshotInputPort getMonthlySalarySnapshotUseCase(
        MonthlySalarySnapshotRepository monthlySalarySnapshotRepository
    ) {
        return new GetMonthlySalarySnapshotUseCase(monthlySalarySnapshotRepository);
    }

    @Bean
    PayMonthlySalaryInputPort payMonthlySalaryUseCase(
        DebtorRepository debtorRepository,
        DebtRepository debtRepository,
        InstallmentRepository installmentRepository,
        SalaryRepository salaryRepository,
        SavingsGoalRepository savingsGoalRepository,
        RecurringExpenseRepository recurringExpenseRepository,
        MonthlySalarySnapshotRepository monthlySalarySnapshotRepository,
        PlatformTransactionManager transactionManager
    ) {
        PayMonthlySalaryUseCase delegate = new PayMonthlySalaryUseCase(
            debtorRepository,
            debtRepository,
            installmentRepository,
            salaryRepository,
            savingsGoalRepository,
            recurringExpenseRepository,
            monthlySalarySnapshotRepository
        );
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return command -> Objects.requireNonNull(tx.execute(status -> delegate.execute(command)));
    }

    @Bean
    GetMonthlySummaryReportInputPort getMonthlySummaryReportUseCase(
        DebtorRepository debtorRepository,
        DebtRepository debtRepository,
        InstallmentRepository installmentRepository,
        MonthlySalarySnapshotRepository monthlySalarySnapshotRepository,
        SalaryRepository salaryRepository,
        SavingsGoalRepository savingsGoalRepository,
        RecurringExpenseRepository recurringExpenseRepository
    ) {
        return new GetMonthlySummaryReportUseCase(
            debtorRepository,
            debtRepository,
            installmentRepository,
            monthlySalarySnapshotRepository,
            salaryRepository,
            savingsGoalRepository,
            recurringExpenseRepository
        );
    }
}
