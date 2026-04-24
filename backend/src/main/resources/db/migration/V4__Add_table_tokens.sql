-- Refresh Token Table
CREATE TABLE tbl_refresh_token
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(255) NOT NULL,
    token      VARCHAR(512) NOT NULL UNIQUE,
    user_agent VARCHAR(255),
    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP
);


-- Indexes for tbl_refresh_token
CREATE INDEX idx_refresh_token_username ON tbl_refresh_token(username);
CREATE INDEX idx_refresh_token_created_at ON tbl_refresh_token(created_at);