-- Board Table
CREATE TABLE tbl_board
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    created_at  TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert initial board-related permissions
INSERT INTO tbl_permission (code, name, description)
VALUES ('board:read', 'Xem thông tin ban', 'Cho phép xem danh sách và chi tiết ban'),
       ('board:create', 'Tạo ban', 'Cho phép tạo ban mới'),
       ('board:update', 'Cập nhật ban', 'Cho phép chỉnh sửa thông tin ban'),
       ('board:delete', 'Xoá ban', 'Cho phép xoá ban');

-- Indexes for tbl_board
CREATE INDEX idx_board_name ON tbl_board(name);