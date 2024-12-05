CREATE TABLE IF NOT EXISTS my_bookshelf_books (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id VARCHAR(255) NOT NULL,
    book_status VARCHAR(100) NOT NULL,
    UNIQUE(user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);