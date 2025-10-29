CREATE TABLE analytic (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    receiver_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    type VARCHAR NOT NULL,
    interval VARCHAR NOT NULL,
    receiver_id timestamptz DEFAULT current_timestamp
);