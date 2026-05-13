CREATE TABLE IF NOT EXISTS household_bag_movements (
    id            VARCHAR(64) PRIMARY KEY,
    bag_id        VARCHAR(64) NOT NULL,
    amount        NUMERIC(14,2) NOT NULL,
    movement_type VARCHAR(20) NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_household_bag_movements_bag
        FOREIGN KEY (bag_id) REFERENCES household_bags (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_household_bag_movements_type
        CHECK (movement_type IN ('EXPENSE', 'ADJUSTMENT', 'RESET'))
);

CREATE INDEX IF NOT EXISTS ix_household_bag_movements_bag_id
    ON household_bag_movements (bag_id);

CREATE INDEX IF NOT EXISTS ix_household_bag_movements_bag_id_created_at
    ON household_bag_movements (bag_id, created_at DESC);
