-- Role Table
CREATE TABLE tbl_role
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(255) NOT NULL UNIQUE,
    name       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

--  Indexes for tbl_role
CREATE INDEX idx_role_name ON tbl_role(code);
CREATE INDEX idx_role_title ON tbl_role(name);

-- Role-Permission Mapping Table
CREATE TABLE tbl_role_permission
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id), 
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES tbl_role (id) ON DELETE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES tbl_permission (id) ON DELETE CASCADE
);

-- Insert initial role-related permissions
INSERT INTO tbl_permission (code, name, description)
VALUES ('role:read', 'Xem thông tin vai trò', 'Cho phép xem danh sách và chi tiết vai trò'),
       ('role:create', 'Tạo vai trò', 'Cho phép tạo vai trò mới'),
       ('role:update', 'Cập nhật vai trò', 'Cho phép chỉnh sửa thông tin vai trò'),
       ('role:delete', 'Xoá vai trò', 'Cho phép xoá vai trò');