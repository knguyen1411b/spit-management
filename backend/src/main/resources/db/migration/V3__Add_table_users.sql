-- User Table
CREATE TABLE tbl_user
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    superuser   BIT(1)       NOT NULL DEFAULT FALSE,
    enabled     BIT(1)       NOT NULL DEFAULT TRUE,
    password_changed BIT(1)   NOT NULL DEFAULT FALSE,
    semester_id BIGINT       NOT NULL,
    created_at  TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for tbl_user
CREATE INDEX idx_user_superuser ON tbl_user(superuser);
CREATE INDEX idx_user_enabled ON tbl_user(enabled);
CREATE INDEX idx_user_semester ON tbl_user(semester_id);

-- User-Role Mapping
CREATE TABLE tbl_user_role
(
    user_id    BIGINT NOT NULL,
    role_id    BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES tbl_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES tbl_role (id) ON DELETE CASCADE
);

-- Insert initial user-related permissions
INSERT INTO tbl_permission (code, name, description)
VALUES ('user:read', 'Xem thông tin người dùng', 'Cho phép xem thông tin chi tiết người dùng'),
       ('user:create', 'Tạo người dùng', 'Cho phép tạo tài khoản người dùng mới'),
       ('user:update', 'Cập nhật người dùng', 'Cho phép chỉnh sửa thông tin người dùng'),
       ('user:delete', 'Xoá người dùng', 'Cho phép xoá tài khoản người dùng');