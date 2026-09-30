CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    CONSTRAINT chk_category_type CHECK (type IN ('income', 'expense'))
);
