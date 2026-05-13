CREATE TABLE IF NOT EXISTS household_bags (
    id               VARCHAR(64) PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    emoji            VARCHAR(32) NOT NULL,
    monthly_amount   NUMERIC(14,2) NOT NULL,
    remaining_amount NUMERIC(14,2) NOT NULL,
    updated_at       TIMESTAMPTZ NOT NULL,
    CONSTRAINT chk_household_bags_name_not_blank CHECK (btrim(name) <> ''),
    CONSTRAINT chk_household_bags_emoji_not_blank CHECK (btrim(emoji) <> ''),
    CONSTRAINT chk_household_bags_monthly_amount_non_negative CHECK (monthly_amount >= 0),
    CONSTRAINT chk_household_bags_remaining_amount_non_negative CHECK (remaining_amount >= 0),
    CONSTRAINT chk_household_bags_remaining_not_above_monthly CHECK (remaining_amount <= monthly_amount)
);

INSERT INTO household_bags (
    id,
    name,
    emoji,
    monthly_amount,
    remaining_amount,
    updated_at
)
SELECT
    CASE category
        WHEN 'VEGETABLES' THEN 'f7f4d9d1-15d8-4f2c-9c8f-5f6a96f9c101'
        WHEN 'GROCERIES' THEN 'c1b71f59-9ea8-4f0d-9153-f5b6a3b21d02'
    END,
    CASE category
        WHEN 'VEGETABLES' THEN 'Verduras'
        WHEN 'GROCERIES' THEN 'Mercadería'
    END,
    CASE category
        WHEN 'VEGETABLES' THEN '🥦'
        WHEN 'GROCERIES' THEN '🛒'
    END,
    monthly_amount,
    remaining_amount,
    updated_at
FROM household_budget_configs
WHERE category IN ('VEGETABLES', 'GROCERIES')
ON CONFLICT (id) DO NOTHING;

INSERT INTO household_bags (
    id,
    name,
    emoji,
    monthly_amount,
    remaining_amount,
    updated_at
)
SELECT
    seed.id,
    seed.name,
    seed.emoji,
    seed.monthly_amount,
    seed.remaining_amount,
    seed.updated_at
FROM (
    VALUES
        ('f7f4d9d1-15d8-4f2c-9c8f-5f6a96f9c101', 'Verduras', '🥦', 0.00::NUMERIC(14,2), 0.00::NUMERIC(14,2), CURRENT_TIMESTAMP),
        ('c1b71f59-9ea8-4f0d-9153-f5b6a3b21d02', 'Mercadería', '🛒', 0.00::NUMERIC(14,2), 0.00::NUMERIC(14,2), CURRENT_TIMESTAMP)
) AS seed(id, name, emoji, monthly_amount, remaining_amount, updated_at)
WHERE NOT EXISTS (
    SELECT 1
    FROM household_bags existing
    WHERE existing.id = seed.id
);
