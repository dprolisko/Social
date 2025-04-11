CREATE TABLE users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    username VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(32) NOT NULL,
    email VARCHAR(64) UNIQUE  NOT NULL,
    phone VARCHAR(16) UNIQUE NOT NULL,
    active boolean default true NOT NULL,
    bio VARCHAR(512),
    createdAt timestamptz DEFAULT current_timestamp,
    updateAt timestamptz DEFAULT current_timestamp
)