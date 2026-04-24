-- Permission Table
CREATE TABLE tbl_permission
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(255) NOT NULL UNIQUE,
    name       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for tbl_permission
CREATE INDEX idx_permission_name ON tbl_permission(code);
CREATE INDEX idx_permission_title ON tbl_permission(name);

-- Insert initial permissions
INSERT INTO tbl_permission (code, name, description)
VALUES ('permission:read', 'Xem thông tin quyền', 'Cho phép xem danh sách quyền hệ thống');