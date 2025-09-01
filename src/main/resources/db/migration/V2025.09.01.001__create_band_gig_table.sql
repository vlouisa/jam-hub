CREATE TABLE jhb_bands (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    record_creation_date_time TIMESTAMP,
    record_creation_user VARCHAR(255),
    record_modification_date_time TIMESTAMP,
    record_modification_user VARCHAR(255)
);

CREATE TABLE jhb_band_members (
    id UUID PRIMARY KEY,
    band_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    record_creation_date_time TIMESTAMP,
    record_creation_user VARCHAR(255),
    record_modification_date_time TIMESTAMP,
    record_modification_user VARCHAR(255),    
        CONSTRAINT fk_band FOREIGN KEY (band_id) REFERENCES jhb_bands(id)
);


CREATE TABLE jhb_gigs (
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    
    -- Embedded Address fields
    venue_address_street VARCHAR(255),
    venue_address_number BIGINT,
    venue_address_city VARCHAR(255),
    venue_address_postal_code VARCHAR(20),
    venue_address_country VARCHAR(100),
    -- End of Embedded Address fields
    
    event_date DATE,
    get_in_time TIME,
    start_time TIME,
    duration BIGINT, 
    band_id UUID NOT NULL,
    status VARCHAR(50),
    record_creation_date_time TIMESTAMP,
    record_creation_user VARCHAR(255),
    record_modification_date_time TIMESTAMP,
    record_modification_user VARCHAR(255),
        CONSTRAINT fk_band FOREIGN KEY (band_id) REFERENCES jhb_bands(id)
);

CREATE TABLE jhb_gig_role_assignments (
    id UUID PRIMARY KEY,
    gig_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    record_creation_date_time TIMESTAMP,
    record_creation_user VARCHAR(255),
    record_modification_date_time TIMESTAMP,
    record_modification_user VARCHAR(255),
        CONSTRAINT fk_gig FOREIGN KEY (gig_id) REFERENCES jhb_gigs(id)
);

