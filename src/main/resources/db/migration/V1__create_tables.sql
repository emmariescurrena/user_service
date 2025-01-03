CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    auth0user_id VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(320) UNIQUE NOT NULL,
    nickname VARCHAR(100),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    bio TEXT,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS favorites (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id VARCHAR(255) NOT NULL,
    UNIQUE(user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS config_preferences (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    value VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS my_bookshelf_books (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id VARCHAR(255) NOT NULL,
    book_status VARCHAR(100) NOT NULL,
    UNIQUE(user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);