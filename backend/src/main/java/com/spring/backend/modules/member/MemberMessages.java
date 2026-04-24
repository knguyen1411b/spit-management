package com.spring.backend.modules.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MemberMessages {
  INDEX_SUCCESS("Lấy danh sách thành viên thành công"),
  SHOW_SUCCESS("Lấy thông tin thành viên thành công"),
  CREATE_SUCCESS("Tạo thành viên thành công"),
  UPDATE_SUCCESS("Cập nhật thành viên thành công"),
  DELETE_SUCCESS("Xóa thành viên thành công"),
  NOT_FOUND("Không tìm thấy thành viên"),
  USER_ALREADY_HAS_MEMBER("Người dùng này đã có hồ sơ thành viên"),
  CANNOT_DELETE("Không thể xóa thành viên đang liên kết với người dùng");

  String message;
}
