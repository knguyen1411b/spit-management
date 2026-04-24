package com.spring.backend.modules.task_request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum TaskRequestMessages {
  INDEX_SUCCESS("Lấy danh sách yêu cầu thành công"),
  PENDING_SUCCESS("Tạo yêu cầu công việc thành công"),
  APPROVE_SUCCESS("Duyệt yêu cầu công việc thành công"),
  REJECT_SUCCESS("Từ chối yêu cầu công việc thành công"),
  PENDING_EXISTED("Yêu cầu công việc đã tồn tại"),
  APPROVED_EXISTED("Yêu cầu công việc đã được duyệt"),
  TASK_REQUEST_NOT_FOUND("Không tìm thấy yêu cầu công việc");

  String message;
}
