-- Task Table
CREATE TABLE tbl_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    date TIMESTAMP  NOT NULL,
    type ENUM('EVENT', 'TASK', 'OTHER') NOT NULL DEFAULT 'TASK',
    semester_id BIGINT NOT NULL,
    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_semester FOREIGN KEY (semester_id) REFERENCES tbl_semester(id)
);

-- Indexes for tbl_task
CREATE INDEX idx_task_semester ON tbl_task(semester_id);
CREATE INDEX idx_task_type ON tbl_task(type);
CREATE INDEX idx_task_date ON tbl_task(date);

-- Insert initial task-related permissions
INSERT INTO tbl_permission (code, name, description)
VALUES
    ('task:read', 'Xem công việc', 'Cho phép xem danh sách và chi tiết công việc'),
    ('task:create', 'Tạo công việc', 'Cho phép tạo công việc mới'),
    ('task:update', 'Cập nhật công việc', 'Cho phép chỉnh sửa thông tin công việc'),
    ('task:delete', 'Xoá công việc', 'Cho phép xoá công việc');