package com.spring.backend.modules.task;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum TaskMessages {
  NOT_FOUND("Không tìm thấy nhiệm vụ"),
  SHOW_ME("Lấy thông tin nhiệm vụ của tôi thành công"),
  ALREADY_EXISTS("Nhiệm vụ đã tồn tại"),
  DELETE_SUCCESS("Xóa nhiệm vụ thành công"),
  UPDATE_SUCCESS("Cập nhật nhiệm vụ thành công"),
  CREATE_SUCCESS("Tạo nhiệm vụ thành công"),
  INDEX_SUCCESS("Lấy danh sách nhiệm vụ thành công"),
  SHOW_SUCCESS("Lấy thông tin nhiệm vụ thành công"),
  MEMBER_NOT_FOUND("Không tìm thấy thành viên trong nhiệm vụ");

  String message;
}
