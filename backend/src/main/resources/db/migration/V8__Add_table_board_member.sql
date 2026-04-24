-- Board-Member-Semester Mapping table
CREATE TABLE tbl_board_member_semester (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT,
    member_id BIGINT,
    semester_id BIGINT,
    position VARCHAR(255) NOT NULL,
    description_board VARCHAR(255),
    created_at TIMESTAMP  DEFAULT NULL,
    updated_at TIMESTAMP  DEFAULT NULL,
    CONSTRAINT fk_bms_board FOREIGN KEY (board_id) REFERENCES tbl_board(id),
    CONSTRAINT fk_bms_member FOREIGN KEY (member_id) REFERENCES tbl_member(id),
    CONSTRAINT fk_bms_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id)
);

-- Indexes for tbl_board_member_semester
CREATE INDEX idx_bms_board ON tbl_board_member_semester(board_id);
CREATE INDEX idx_bms_member ON tbl_board_member_semester(member_id);
CREATE INDEX idx_bms_semester ON tbl_board_member_semester(semester_id);

-- Insert initial board member-related permissions
INSERT INTO tbl_permission (code, name, description)
VALUES
    ('board_member:read', 'Xem tất cả thành viên của ban', 'Cho phép xem thông tin chi tiết các thành viên trong ban'),
    ('board_member:create', 'Thêm thành viên vào ban', 'Cho phép thêm thành viên vào ban'),
    ('board_member:update', 'Cập nhật chi tiết thành viên của ban', 'Cho phép chỉnh sửa thông tin thành viên của ban'),
    ('board_member:delete', 'Xoá thành viên của ban', 'Cho phép xoá thành viên khỏi ban');