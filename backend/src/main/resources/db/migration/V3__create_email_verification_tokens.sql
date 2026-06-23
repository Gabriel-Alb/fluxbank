CREATE TABLE email_verification_tokens (
    id UUID PRIMARY KEY DEFAULT uuidv7(),

    user_id UUID NOT NULL,

    token_hash BYTEA NOT NULL,

    expires_at TIMESTAMPTZ NOT NULL,

    used_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_email_verification_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,

    CONSTRAINT uk_email_verification_tokens_token_hash
        UNIQUE (token_hash)
);

CREATE INDEX idx_email_verification_tokens_user_id
    ON email_verification_tokens (user_id);

CREATE INDEX idx_email_verification_tokens_expires_at
    ON email_verification_tokens (expires_at);