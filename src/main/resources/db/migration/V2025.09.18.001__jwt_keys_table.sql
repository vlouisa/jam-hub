CREATE TABLE jwt_keys (
    id UUID PRIMARY KEY,
    kid UUID NOT NULL UNIQUE,
    public_key TEXT NOT NULL,
    private_key TEXT NOT NULL,
    status VARCHAR(50) NOT NULL
);

