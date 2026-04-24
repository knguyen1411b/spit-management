package com.spring.backend.modules.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum UserMessages {
  INDEX_SUCCESS("Lấy danh sách người dùng thành công"),
  SHOW_SUCCESS("Lấy thông tin người dùng thành công"),
  CREATE_SUCCESS("Tạo người dùng thành công"),
  UPDATE_SUCCESS("Cập nhật người dùng thành công"),
  UPDATE_SEMESTER_SUCCESS("Cập nhật học kỳ người dùng thành công"),
  DELETE_SUCCESS("Xóa người dùng thành công"),
  WITH_ROLE_NOT_FOUND("Không tìm thấy vai trò của người dùng"),
  NOT_FOUND("Không tìm thấy người dùng");
  String message;
}
