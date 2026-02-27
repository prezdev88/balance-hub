package cl.prezdev.balancehub.application.usecases.financialplan;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import cl.prezdev.balancehub.application.ports.in.GetMonthlyFreeAmountInputPort;
import cl.prezdev.balancehub.application.ports.out.RecurringExpenseRepository;
import cl.prezdev.balancehub.application.ports.out.SalaryRepository;
import cl.prezdev.balancehub.application.ports.out.SavingsGoalRepository;
import cl.prezdev.balancehub.domain.enums.ExpenseType;

public class GetMonthlyFreeAmountUseCase implements GetMonthlyFreeAmountInputPort {

    private final SalaryRepository salaryRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;

    public GetMonthlyFreeAmountUseCase(
        SalaryRepository salaryRepository,
        SavingsGoalRepository savingsGoalRepository,
        RecurringExpenseRepository recurringExpenseRepository
    ) {
        if (salaryRepository == null) {
            throw new IllegalArgumentException("SalaryRepository cannot be null");
        }
        if (savingsGoalRepository == null) {
            throw new IllegalArgumentException("SavingsGoalRepository cannot be null");
        }
        if (recurringExpenseRepository == null) {
            throw new IllegalArgumentException("RecurringExpenseRepository cannot be null");
        }
        this.salaryRepository = salaryRepository;
        this.savingsGoalRepository = savingsGoalRepository;
        this.recurringExpenseRepository = recurringExpenseRepository;
    }

    @Override
    public GetMonthlyFreeAmountResult execute(GetMonthlyFreeAmountCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("command must not be null");
        }
        if (command.year() < 1970 || command.year() > 9999) {
            throw new IllegalArgumentException("year must be between 1970 and 9999");
        }

        var currentSalary = salaryRepository.findActive().map(salary -> salary.getAmount()).orElse(BigDecimal.ZERO);
        var monthlySavingsGoal = savingsGoalRepository.findActive()
            .map(savingsGoal -> savingsGoal.getAmount())
            .orElse(BigDecimal.ZERO);
        var monthlyFixedExpenses = recurringExpenseRepository.totalByType(ExpenseType.FIXED);
        var monthlyOptionalExpenses = recurringExpenseRepository.totalByType(ExpenseType.OPTIONAL);
        var monthlyFreeAmount = currentSalary
            .subtract(monthlySavingsGoal)
            .subtract(monthlyFixedExpenses)
            .subtract(monthlyOptionalExpenses);

        var months = IntStream.rangeClosed(1, 12)
            .mapToObj(month -> new MonthlyFreeAmountItem(month, command.year(), monthlyFreeAmount))
            .toList();

        return new GetMonthlyFreeAmountResult(
            command.year(),
            currentSalary,
            monthlySavingsGoal,
            monthlyFixedExpenses,
            monthlyOptionalExpenses,
            monthlyFreeAmount,
            months
        );
    }
}
