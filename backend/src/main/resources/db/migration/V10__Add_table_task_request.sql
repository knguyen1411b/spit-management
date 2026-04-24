-- Task-Member Mapping Table
CREATE TABLE tbl_task_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,

    status ENUM('NONE','PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'NONE',

    requested_at TIMESTAMP DEFAULT NULL,
    approved_at TIMESTAMP DEFAULT NULL,

    description VARCHAR(1000) DEFAULT NULL,

    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_member_task FOREIGN KEY (task_id) REFERENCES tbl_task(id),
    CONSTRAINT fk_task_member_member FOREIGN KEY (member_id) REFERENCES tbl_member(id)
);


-- Insert initial permissions for task requests
INSERT INTO tbl_permission (code, name, description)
VALUES
    ('task_request:read', 'Xem yêu cầu duyệt công việc', 'Cho phép xem các yêu cầu công việc'),
    ('task_request:approve', 'Duyệt yêu cầu công việc', 'Cho phép duyệt các yêu cầu công việc');