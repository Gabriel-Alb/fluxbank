ALTER TABLE email_verification_tokens
ADD COLUMN revoked_at TIMESTAMPTZ;

CREATE INDEX idx_email_verification_tokens_revoked_at
    ON email_verification_tokens (revoked_at);