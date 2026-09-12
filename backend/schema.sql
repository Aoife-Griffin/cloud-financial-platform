CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
); ---Came up automatically, changed id to bigserial to avoid running out of ids,  timestamp default to timestamp with time zone default to keep time zones in mind

CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT ON DELETE CASCADE NOT NULL, -- added in NOT NULL so that the account has to have an owner
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL, --- for different types of accounts (checking, savings, credit carf)
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0, --- changed default to 0 so created account has a balance
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL

    constraint fk_account_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,

    CONSTRAINT chk_category_type CHECK (type IN ('income', 'expense'))
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT REFERENCES accounts(id) ON DELETE CASCADE NOT NULL,
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    amount DECIMAL(15, 2) NOT NULL,
    type VARCHAR(50) NOT NULL, --- for different types of transactions (deposit, withdrawal, transfer)
    description TEXT,
    transaction_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    constraint fk_transaction_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
    constraint fk_transaction_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
    constraint chk_transaction_type CHECK (type IN ('deposit', 'withdrawal', 'transfer'))
    constraint chk_transaction_amount CHECK (amount > 0)
);

CREATE TABLE budgets(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    name VARCHAR(100) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    constraint fk_budget_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    constraint chk_budget_amount CHECK (amount > 0),
    constraint chk_budget_dates CHECK (start_date < end_date)
);

CREATE TABLE budget_categories (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT REFERENCES budgets(id) ON DELETE CASCADE NOT NULL,
    category_id BIGINT REFERENCES categories(id) ON DELETE CASCADE NOT NULL,

    constraint fk_budget_category_budget FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE,
    constraint fk_budget_category_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);