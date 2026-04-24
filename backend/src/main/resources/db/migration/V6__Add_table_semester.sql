-- Semester Table
CREATE TABLE tbl_semester (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255), 
    start_date TIMESTAMP  NOT NULL,
    end_date TIMESTAMP  NOT NULL,
    semester_order INT NOT NULL,
    current BIT(1) NOT NULL, 
    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for tbl_semester
CREATE INDEX idx_semester_order ON tbl_semester(semester_order);
CREATE INDEX idx_semester_current ON tbl_semester(current);
CREATE INDEX idx_semester_start ON tbl_semester(start_date);
CREATE INDEX idx_semester_end ON tbl_semester(end_date);

-- Semester-Member Mapping Table
CREATE TABLE tbl_semester_member (
    semester_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    PRIMARY KEY (semester_id, member_id),
    CONSTRAINT fk_semester_member_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id) ON DELETE CASCADE,
    CONSTRAINT fk_semester_member_member FOREIGN KEY (member_id) REFERENCES tbl_member(id) ON DELETE CASCADE
);

-- Indexes for tbl_semester_member
CREATE INDEX idx_semester_member_semester ON tbl_semester_member(semester_id);
CREATE INDEX idx_semester_member_member ON tbl_semester_member(member_id);

-- Insert initial semester-related permissions
INSERT INTO tbl_permission (code, name, description)
VALUES
    ('semester:read', 'Xem thông tin học kỳ', 'Cho phép xem chi tiết các học kỳ'),
    ('semester:create', 'Tạo học kỳ', 'Cho phép tạo học kỳ mới'),
    ('semester:update', 'Cập nhật học kỳ', 'Cho phép chỉnh sửa thông tin học kỳ'),
    ('semester:delete', 'Xoá học kỳ', 'Cho phép xoá học kỳ');