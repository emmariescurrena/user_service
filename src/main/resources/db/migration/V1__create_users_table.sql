CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    auth0user_id VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(320) UNIQUE NOT NULL,
    nickname VARCHAR(100),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    bio TEXT,
    role VARCHAR(50) NOT NULL
);