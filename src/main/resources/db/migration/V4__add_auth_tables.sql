CREATE TABLE IF NOT EXISTS auth_users (
    id              VARCHAR(64) PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(20) NOT NULL,
    debtor_id       VARCHAR(64) NULL,
    enabled         BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL,
    updated_at      TIMESTAMPTZ NOT NULL,
    CONSTRAINT chk_auth_users_email_not_blank CHECK (btrim(email) <> ''),
    CONSTRAINT chk_auth_users_password_not_blank CHECK (btrim(password_hash) <> ''),
    CONSTRAINT chk_auth_users_role CHECK (role IN ('ADMIN', 'DEBTOR')),
    CONSTRAINT fk_auth_users_debtor
        FOREIGN KEY (debtor_id) REFERENCES debtors (id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_auth_users_debtor_id
    ON auth_users (debtor_id)
    WHERE debtor_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS ix_auth_users_role
    ON auth_users (role);

CREATE TABLE IF NOT EXISTS auth_sessions (
    token           VARCHAR(128) PRIMARY KEY,
    user_id         VARCHAR(64) NOT NULL,
    expires_at      TIMESTAMPTZ NOT NULL,
    revoked         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL,
    last_used_at    TIMESTAMPTZ NULL,
    CONSTRAINT fk_auth_sessions_user
        FOREIGN KEY (user_id) REFERENCES auth_users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS ix_auth_sessions_user_id
    ON auth_sessions (user_id);

CREATE INDEX IF NOT EXISTS ix_auth_sessions_expires_revoked
    ON auth_sessions (expires_at, revoked);
