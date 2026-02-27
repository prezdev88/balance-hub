CREATE TABLE IF NOT EXISTS savings_goals (
    id              VARCHAR(64) PRIMARY KEY,
    amount          NUMERIC(14,2) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL,
    active          BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_savings_goals_amount_positive CHECK (amount > 0)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_savings_goals_single_active
    ON savings_goals (active)
    WHERE active = TRUE;
