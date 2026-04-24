package com.spring.backend.modules.notification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum NotificationMessages {
  UNREAD_COUNT_SUCCESS("Lấy số lượng thông báo chưa đọc thành công"),
  INDEX_SUCCESS("Lấy danh sách thông báo thành công"),
  CREATED_SUCCESS("Tạo thông báo thành công"),
  MARK_READ_SUCCESS("Đã đánh dấu thông báo là đã đọc"),
  NOT_FOUND("Không tìm thấy thông báo"),
  ACCESS_DENIED("Bạn không có quyền truy cập thông báo này");

  String message;
}
