package cl.prezdev.balancehub.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    CreateDebtorUseCase createDebtorUseCase(DebtorRepository debtorRepository) {
        return new CreateDebtorUseCase(debtorRepository);
    }

    @Bean
    ListDebtorsUseCase listDebtorsUseCase(DebtorRepository debtorRepository) {
        return new ListDebtorsUseCase(debtorRepository);
    }

    @Bean
    CreateDebtUseCase createDebtUseCase(DebtRepository debtRepository, InstallmentRepository installmentRepository) {
        return new CreateDebtUseCase(debtRepository, installmentRepository);
    }

    @Bean
    GetDebtsUseCase getDebtsUseCase(
        DebtRepository debtRepository,
        DebtorRepository debtorRepository,
        InstallmentRepository installmentRepository
    ) {
        return new GetDebtsUseCase(debtRepository, debtorRepository, installmentRepository);
    }

    @Bean
    PayInstallmentUseCase payInstallmentUseCase(InstallmentRepository installmentRepository) {
        return new PayInstallmentUseCase(installmentRepository);
    }

    @Bean
    CreateRecurringExpenseUseCase createRecurringExpenseUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new CreateRecurringExpenseUseCase(recurringExpenseRepository);
    }

    @Bean
    ListRecurringExpensesUseCase listRecurringExpensesUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new ListRecurringExpensesUseCase(recurringExpenseRepository);
    }

    @Bean
    UpdateFixedExpenseUseCase updateFixedExpenseUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new UpdateFixedExpenseUseCase(recurringExpenseRepository);
    }

    @Bean
    GetRecurringExpenseTotalUseCase getRecurringExpenseTotalUseCase(RecurringExpenseRepository recurringExpenseRepository) {
        return new GetRecurringExpenseTotalUseCase(recurringExpenseRepository);
    }

    @Bean
    CreateSalaryUseCase createSalaryUseCase(SalaryRepository salaryRepository) {
        return new CreateSalaryUseCase(salaryRepository);
    }
}
