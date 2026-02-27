CREATE TABLE IF NOT EXISTS monthly_salary_snapshots (
    id                          VARCHAR(64) PRIMARY KEY,
    debtor_id                   VARCHAR(64) NOT NULL,
    year                        INTEGER NOT NULL,
    month                       INTEGER NOT NULL,
    monthly_free_amount         NUMERIC(14,2) NOT NULL,
    half_free_amount            NUMERIC(14,2) NOT NULL,
    total_installments_amount   NUMERIC(14,2) NOT NULL,
    salary_column_amount        NUMERIC(14,2) NOT NULL,
    status                      VARCHAR(20) NOT NULL,
    created_at                  TIMESTAMPTZ NOT NULL,
    paid_at                     TIMESTAMPTZ NULL,
    CONSTRAINT fk_monthly_salary_snapshots_debtor
        FOREIGN KEY (debtor_id) REFERENCES debtors (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_monthly_salary_snapshots_month CHECK (month BETWEEN 1 AND 12),
    CONSTRAINT chk_monthly_salary_snapshots_status CHECK (status IN ('PENDING', 'PAID')),
    CONSTRAINT ux_monthly_salary_snapshots_debtor_period UNIQUE (debtor_id, year, month)
);

CREATE INDEX IF NOT EXISTS ix_monthly_salary_snapshots_year_month
    ON monthly_salary_snapshots (year, month);
