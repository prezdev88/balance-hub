CREATE TABLE IF NOT EXISTS household_budget_configs (
    category        VARCHAR(20) PRIMARY KEY,
    monthly_amount  NUMERIC(14,2) NOT NULL,
    remaining_amount NUMERIC(14,2) NOT NULL,
    updated_at      TIMESTAMPTZ NOT NULL,
    CONSTRAINT chk_household_budget_configs_category CHECK (category IN ('VEGETABLES', 'GROCERIES')),
    CONSTRAINT chk_household_budget_configs_amount_positive CHECK (monthly_amount > 0),
    CONSTRAINT chk_household_budget_configs_remaining_amount_non_negative CHECK (remaining_amount >= 0)
);
