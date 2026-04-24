-- Members table
CREATE TABLE tbl_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    last_name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    gender BIT(1) NOT NULL,
    birthday TIMESTAMP  NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    class_name VARCHAR(255) NOT NULL,
    avatar VARCHAR(255),
    generation VARCHAR(255) NOT NULL,
    username VARCHAR(255),
    description VARCHAR(255), 
    user_id BIGINT UNIQUE, 
    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES tbl_user(id)
);

-- Indexes for tbl_member
CREATE INDEX idx_member_email ON tbl_member(email);
CREATE INDEX idx_member_username ON tbl_member(username);
CREATE INDEX idx_member_class_name ON tbl_member(class_name);
CREATE INDEX idx_member_generation ON tbl_member(generation);
CREATE INDEX idx_member_last_name ON tbl_member(last_name);
CREATE INDEX idx_member_first_name ON tbl_member(first_name);

-- Insert initial member-related permissions
INSERT INTO tbl_permission (code, name, description)
VALUES
    ('member:read', 'Xem thông tin thành viên', 'Cho phép xem thông tin chi tiết thành viên'),
    ('member:create', 'Tạo thành viên', 'Cho phép tạo tài khoản thành viên mới'),
    ('member:update', 'Cập nhật thành viên', 'Cho phép chỉnh sửa thông tin thành viên'),
    ('member:delete', 'Xoá thành viên', 'Cho phép xoá tài khoản thành viên');