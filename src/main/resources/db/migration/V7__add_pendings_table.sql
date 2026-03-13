CREATE TABLE IF NOT EXISTS pendings (
    id          VARCHAR(64) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL,
    CONSTRAINT chk_pendings_description_not_blank CHECK (btrim(description) <> '')
);
