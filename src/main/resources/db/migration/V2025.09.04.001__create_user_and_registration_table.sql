CREATE TABLE jhb_users (
    id UUID PRIMARY KEY,
    display_name VARCHAR(255) NOT NULL ,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL ,
    record_creation_date_time TIMESTAMP,
    record_creation_user UUID,
    record_modification_date_time TIMESTAMP,
    record_modification_user UUID
);

CREATE TABLE jhb_user_registrations (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    verified_at TIMESTAMP,
    expired_at TIMESTAMP,
    revoked_at TIMESTAMP,
    record_creation_date_time TIMESTAMP,
    record_creation_user UUID,
    record_modification_date_time TIMESTAMP,
    record_modification_user UUID
);

