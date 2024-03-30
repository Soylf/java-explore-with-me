CREATE TABLE IF NOT EXISTS endpoint_hit (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app VARCHAR(150) NOT NULL,
    uri VARCHAR(150) NOT NULL,
    ip VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE
);