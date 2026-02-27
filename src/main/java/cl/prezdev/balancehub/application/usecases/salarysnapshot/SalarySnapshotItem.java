package cl.prezdev.balancehub.application.usecases.salarysnapshot;

import java.math.BigDecimal;
import java.time.Instant;

import cl.prezdev.balancehub.domain.enums.SalarySnapshotStatus;

public record SalarySnapshotItem(
    String id,
    String debtorId,
    int year,
    int month,
    BigDecimal monthlyFreeAmount,
    BigDecimal halfFreeAmount,
    BigDecimal totalInstallmentsAmount,
    BigDecimal salaryColumnAmount,
    SalarySnapshotStatus status,
    Instant createdAt,
    Instant paidAt
) {}
