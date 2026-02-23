package cl.prezdev.balancehub.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.prezdev.balancehub.application.ports.in.CreateDebtInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateDebtorInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateRecurringExpenseInputPort;
import cl.prezdev.balancehub.application.ports.in.CreateSalaryInputPort;
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
import cl.prezdev.balancehub.application.usecases.recurringexpense.list.ListRecurringExpensesUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.total.GetRecurringExpenseTotalUseCase;
import cl.prezdev.balancehub.application.usecases.recurringexpense.update.UpdateFixedExpenseUseCase;
import cl.prezdev.balancehub.application.usecases.salary.create.CreateSalaryUseCase;

@Configuration
public class UseCaseConfiguration {

    @Bean
    CreateDebtorInputPort createDebtorUseCase(DebtorRepository debtorRepository) {
        return new CreateDebtorUseCase(debtorRepository);
    }

    @Bean
    ListDebtorsInputPort listDebtorsUseCase(DebtorRepository debtorRepository) {
        return new ListDebtorsUseCase(debtorRepository);
    }

    @Bean
    CreateDebtInputPort createDebtUseCase(DebtRepository debtRepository, InstallmentRepository installmentRepository) {
        return new CreateDebtUseCase(debtRepository, installmentRepository);
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
    PayInstallmentInputPort payInstallmentUseCase(InstallmentRepository installmentRepository) {
        return new PayInstallmentUseCase(installmentRepository);
    }

    @Bean
    CreateRecurringExpenseInputPort createRecurringExpenseUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new CreateRecurringExpenseUseCase(recurringExpenseRepository);
    }

    @Bean
    ListRecurringExpensesInputPort listRecurringExpensesUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new ListRecurringExpensesUseCase(recurringExpenseRepository);
    }

    @Bean
    UpdateFixedExpenseInputPort updateFixedExpenseUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new UpdateFixedExpenseUseCase(recurringExpenseRepository);
    }

    @Bean
    GetRecurringExpenseTotalInputPort getRecurringExpenseTotalUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new GetRecurringExpenseTotalUseCase(recurringExpenseRepository);
    }

    @Bean
    CreateSalaryInputPort createSalaryUseCase(SalaryRepository salaryRepository) {
        return new CreateSalaryUseCase(salaryRepository);
    }
}
