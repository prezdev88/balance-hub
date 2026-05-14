ALTER TABLE household_bags
ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;

CREATE INDEX IF NOT EXISTS ix_household_bags_active
    ON household_bags (active);
