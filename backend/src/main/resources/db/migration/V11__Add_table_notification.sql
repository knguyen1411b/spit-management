CREATE TABLE tbl_notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    type ENUM('INFO', 'WARNING', 'ALERT') NOT NULL DEFAULT 'INFO',
    sender_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_sender FOREIGN KEY (sender_id) REFERENCES tbl_user(id)
);

CREATE TABLE tbl_notification_receiver (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    notification_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP NULL,
    CONSTRAINT fk_receiver_notification FOREIGN KEY (notification_id) REFERENCES tbl_notification(id),
    CONSTRAINT fk_receiver_user FOREIGN KEY (receiver_id) REFERENCES tbl_user(id),
    UNIQUE (notification_id, receiver_id)
);

CREATE INDEX idx_notification_sender ON tbl_notification(sender_id);
CREATE INDEX idx_notification_receiver ON tbl_notification_receiver(receiver_id);
CREATE INDEX idx_notification_is_read ON tbl_notification_receiver(is_read);

INSERT INTO tbl_permission (code, name, description)
VALUES
    ('notification:create', 'Tạo thông báo','Cho phép người dùng tạo và gửi thông báo');
