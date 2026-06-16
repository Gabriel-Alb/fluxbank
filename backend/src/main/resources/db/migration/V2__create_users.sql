CREATE TABLE users (
    id UUID NOT NULL DEFAULT uuidv7(),

    full_name VARCHAR(150) NOT NULL,

    email_encrypted BYTEA NOT NULL,
    email_nonce BYTEA NOT NULL,
    email_lookup_hash BYTEA NOT NULL,
    email_key_version SMALLINT NOT NULL DEFAULT 1,

    cpf_encrypted BYTEA NOT NULL,
    cpf_nonce BYTEA NOT NULL,
    cpf_lookup_hash BYTEA NOT NULL,
    cpf_key_version SMALLINT NOT NULL DEFAULT 1,

    password_hash VARCHAR(255) NOT NULL,

    status VARCHAR(32) NOT NULL DEFAULT 'PENDING_VERIFICATION',
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,

    failed_login_attempts INTEGER NOT NULL DEFAULT 0,
    locked_until TIMESTAMP WITH TIME ZONE,

    password_changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_users
        PRIMARY KEY (id),

    CONSTRAINT uk_users_email_lookup_hash
        UNIQUE (email_lookup_hash),

    CONSTRAINT uk_users_cpf_lookup_hash
        UNIQUE (cpf_lookup_hash),

    CONSTRAINT ck_users_full_name_not_blank
        CHECK (btrim(full_name) <> ''),

    CONSTRAINT ck_users_email_encrypted_not_empty
        CHECK (octet_length(email_encrypted) > 0),

    CONSTRAINT ck_users_email_nonce_length
        CHECK (octet_length(email_nonce) = 12),

    CONSTRAINT ck_users_email_lookup_hash_length
        CHECK (octet_length(email_lookup_hash) = 32),

    CONSTRAINT ck_users_email_key_version
        CHECK (email_key_version > 0),

    CONSTRAINT ck_users_cpf_encrypted_not_empty
        CHECK (octet_length(cpf_encrypted) > 0),

    CONSTRAINT ck_users_cpf_nonce_length
        CHECK (octet_length(cpf_nonce) = 12),

    CONSTRAINT ck_users_cpf_lookup_hash_length
        CHECK (octet_length(cpf_lookup_hash) = 32),

    CONSTRAINT ck_users_cpf_key_version
        CHECK (cpf_key_version > 0),

    CONSTRAINT ck_users_password_hash_not_blank
        CHECK (password_hash <> ''),

    CONSTRAINT ck_users_status
        CHECK (
            status IN (
                'PENDING_VERIFICATION',
                'ACTIVE',
                'SUSPENDED',
                'DEACTIVATED'
            )
        ),

    CONSTRAINT ck_users_failed_login_attempts
        CHECK (failed_login_attempts >= 0)
);