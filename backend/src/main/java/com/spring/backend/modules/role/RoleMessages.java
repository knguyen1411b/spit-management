package com.spring.backend.modules.role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RoleMessages {
  INDEX_SUCCESS("Lấy danh sách vai trò thành công"),
  SHOW_SUCCESS("Lấy thông tin vai trò thành công"),
  CREATE_SUCCESS("Tạo vai trò thành công"),
  UPDATE_SUCCESS("Cập nhật vai trò thành công"),
  DELETE_SUCCESS("Xóa vai trò thành công"),
  CODE_EXISTS("Mã vai trò đã tồn tại"),
  NOT_FOUND("Không tìm thấy vai trò");
  String message;
}
