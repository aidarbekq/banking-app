CREATE TABLE IF NOT EXISTS users (
                                     id              BIGSERIAL PRIMARY KEY,
                                     name            VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    registration_date TIMESTAMP  NOT NULL
    );

CREATE TABLE IF NOT EXISTS accounts (
                                        id          BIGSERIAL PRIMARY KEY,
                                        account_number VARCHAR(100) NOT NULL UNIQUE,
    balance     DECIMAL(15, 2) NOT NULL,
    user_id     BIGINT NOT NULL,
    CONSTRAINT fk_user
    FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE
    );