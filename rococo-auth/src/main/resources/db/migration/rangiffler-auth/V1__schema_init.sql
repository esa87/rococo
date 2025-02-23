CREATE TABLE IF NOT EXISTS `user` (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID(), true)) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    account_non_expired BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS `authority` (
    id BINARY(16) NOT NULL DEFAULT (UUID_TO_BIN(UUID(), true)),
    user_id BINARY(16) NOT NULL,
    authority ENUM('read', 'write') NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_authorities_users FOREIGN KEY (user_id) REFERENCES `user` (id)
);
