package cl.prezdev.balancehub.infrastructure.web;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.prezdev.balancehub.application.ports.in.GetMonthlyFreeAmountInputPort;
import cl.prezdev.balancehub.application.usecases.financialplan.GetMonthlyFreeAmountCommand;
import cl.prezdev.balancehub.application.usecases.financialplan.GetMonthlyFreeAmountResult;
import cl.prezdev.balancehub.application.usecases.financialplan.MonthlyFreeAmountItem;

@RestController
@RequestMapping("/api/financial-plan")
public class FinancialPlanController {

    private final GetMonthlyFreeAmountInputPort getMonthlyFreeAmountUseCase;

    public FinancialPlanController(GetMonthlyFreeAmountInputPort getMonthlyFreeAmountUseCase) {
        this.getMonthlyFreeAmountUseCase = getMonthlyFreeAmountUseCase;
    }

    @GetMapping("/monthly-free-amount")
    public ResponseEntity<GetMonthlyFreeAmountHttpResponse> getMonthlyFreeAmount(@RequestParam(required = false) Integer year) {
        int targetYear = year != null ? year : Year.now().getValue();
        GetMonthlyFreeAmountResult result = getMonthlyFreeAmountUseCase.execute(new GetMonthlyFreeAmountCommand(targetYear));
        return ResponseEntity.ok(toHttpResponse(result));
    }

    private static GetMonthlyFreeAmountHttpResponse toHttpResponse(GetMonthlyFreeAmountResult result) {
        return new GetMonthlyFreeAmountHttpResponse(
            result.year(),
            result.currentSalary(),
            result.monthlySavingsGoal(),
            result.monthlyFixedExpenses(),
            result.monthlyOptionalExpenses(),
            result.monthlyFreeAmount(),
            result.months().stream().map(FinancialPlanController::toHttpItem).toList()
        );
    }

    private static MonthlyFreeAmountHttpItem toHttpItem(MonthlyFreeAmountItem item) {
        return new MonthlyFreeAmountHttpItem(item.month(), item.year(), item.freeAmount());
    }

    public record GetMonthlyFreeAmountHttpResponse(
        int year,
        BigDecimal currentSalary,
        BigDecimal monthlySavingsGoal,
        BigDecimal monthlyFixedExpenses,
        BigDecimal monthlyOptionalExpenses,
        BigDecimal monthlyFreeAmount,
        List<MonthlyFreeAmountHttpItem> months
    ) {}

    public record MonthlyFreeAmountHttpItem(
        int month,
        int year,
        BigDecimal freeAmount
    ) {}
}
