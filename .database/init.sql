-- Balance Hub - PostgreSQL schema (base inicial)
-- Basado en las entidades de dominio actuales y puertos de repositorio.

BEGIN;

CREATE TABLE IF NOT EXISTS debtors (
    id              VARCHAR(64) PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    CONSTRAINT chk_debtors_name_not_blank CHECK (btrim(name) <> ''),
    CONSTRAINT chk_debtors_email_not_blank CHECK (btrim(email) <> '')
);

CREATE TABLE IF NOT EXISTS salaries (
    id              VARCHAR(64) PRIMARY KEY,
    amount          NUMERIC(14,2) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL,
    active          BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_salaries_amount_positive CHECK (amount > 0)
);

-- Un solo salario activo a la vez (alineado con deactivateCurrentSalary + save).
CREATE UNIQUE INDEX IF NOT EXISTS ux_salaries_single_active
    ON salaries (active)
    WHERE active = TRUE;

CREATE TABLE IF NOT EXISTS recurring_expenses (
    id              VARCHAR(64) PRIMARY KEY,
    description     VARCHAR(255) NOT NULL,
    amount          NUMERIC(14,2) NOT NULL,
    type            VARCHAR(20) NOT NULL,
    CONSTRAINT chk_recurring_expenses_description_not_blank CHECK (btrim(description) <> ''),
    CONSTRAINT chk_recurring_expenses_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_recurring_expenses_type CHECK (type IN ('FIXED', 'OPTIONAL'))
);

CREATE INDEX IF NOT EXISTS ix_recurring_expenses_type
    ON recurring_expenses (type);

CREATE TABLE IF NOT EXISTS debts (
    id              VARCHAR(64) PRIMARY KEY,
    description     VARCHAR(255) NOT NULL,
    total_amount    NUMERIC(14,2) NOT NULL,
    debtor_id       VARCHAR(64) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL,
    settled         BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_debts_debtor
        FOREIGN KEY (debtor_id) REFERENCES debtors (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_debts_description_not_blank CHECK (btrim(description) <> ''),
    CONSTRAINT chk_debts_total_amount_positive CHECK (total_amount > 0)
);

-- Soporta findByDebtorId y findByDebtorIdAndDateRange (por fecha de creación).
CREATE INDEX IF NOT EXISTS ix_debts_debtor_id
    ON debts (debtor_id);

CREATE INDEX IF NOT EXISTS ix_debts_debtor_id_created_at
    ON debts (debtor_id, created_at);

CREATE TABLE IF NOT EXISTS installments (
    id              VARCHAR(64) PRIMARY KEY,
    debt_id         VARCHAR(64) NOT NULL,
    installment_no  INTEGER NOT NULL,
    due_date        DATE NOT NULL,
    paid_at         TIMESTAMPTZ NULL,
    amount          NUMERIC(14,2) NOT NULL,
    CONSTRAINT fk_installments_debt
        FOREIGN KEY (debt_id) REFERENCES debts (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT chk_installments_number_positive CHECK (installment_no > 0),
    CONSTRAINT chk_installments_amount_positive CHECK (amount > 0),
    CONSTRAINT ux_installments_debt_number UNIQUE (debt_id, installment_no)
);

-- Soporta findByDebtIds y ordenamiento por número.
CREATE INDEX IF NOT EXISTS ix_installments_debt_id
    ON installments (debt_id);

CREATE INDEX IF NOT EXISTS ix_installments_debt_id_number
    ON installments (debt_id, installment_no);

COMMIT;
