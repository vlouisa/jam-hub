CREATE TABLE jhb_users (
    id UUID PRIMARY KEY,
    display_name VARCHAR(255) NOT NULL ,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL ,
    record_creation_date_time TIMESTAMP,
    record_creation_user VARCHAR(255),
    record_modification_date_time TIMESTAMP,
    record_modification_user VARCHAR(255)
);

CREATE TABLE jhb_user_registrations (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    expiration_date_time TIMESTAMP,
    revocation_date_time TIMESTAMP,
    record_creation_date_time TIMESTAMP,
    record_creation_user VARCHAR(255),
    record_modification_date_time TIMESTAMP,
    record_modification_user VARCHAR(255)
);

