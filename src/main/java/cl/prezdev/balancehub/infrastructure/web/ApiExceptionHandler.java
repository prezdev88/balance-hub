package cl.prezdev.balancehub.infrastructure.web;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.prezdev.balancehub.application.exception.DebtorNotFoundException;
import cl.prezdev.balancehub.application.exception.DebtNotFoundException;
import cl.prezdev.balancehub.application.exception.MonthlySalarySnapshotNotFoundException;
import cl.prezdev.balancehub.application.exception.FixedExpenseNotFoundException;
import cl.prezdev.balancehub.application.exception.InstallmentNotFoundException;
import cl.prezdev.balancehub.domain.exception.InvalidDebtException;
import cl.prezdev.balancehub.domain.exception.InvalidDebtorException;
import cl.prezdev.balancehub.domain.exception.InvalidInstallmentException;
import cl.prezdev.balancehub.domain.exception.InvalidRecurringExpenseException;
import cl.prezdev.balancehub.domain.exception.InvalidSavingsGoalException;
import cl.prezdev.balancehub.domain.exception.InvalidSalaryException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({
        IllegalArgumentException.class,
        InvalidDebtorException.class,
        InvalidDebtException.class,
        InvalidInstallmentException.class,
        InvalidRecurringExpenseException.class,
        InvalidSavingsGoalException.class,
        InvalidSalaryException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
            ));
    }

    @ExceptionHandler({
        DebtorNotFoundException.class,
        DebtNotFoundException.class,
        MonthlySalarySnapshotNotFoundException.class,
        InstallmentNotFoundException.class,
        FixedExpenseNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
            ));
    }

    public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message
    ) {}
}
