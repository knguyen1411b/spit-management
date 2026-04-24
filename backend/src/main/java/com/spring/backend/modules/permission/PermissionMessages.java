package com.spring.backend.modules.permission;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PermissionMessages {
  INDEX_SUCCESS("Lấy danh sách quyền thành công"),
  SHOW_SUCCESS("Lấy quyền thành công"),
  NOT_FOUND("Quyền không tồn tại");

  String message;
}
