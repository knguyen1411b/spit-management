package com.spring.backend.modules.board_member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum BoardMemberMessages {
  NOT_FOUND("Không tìm thấy thành viên trong bảng"),
  INDEX_SUCCESS("Lấy danh sách thành viên trong bảng thành công"),
  SHOW_SUCCESS("Lấy thông tin thành viên trong bảng thành công"),
  CREATE_SUCCESS("Thêm thành viên vào bảng thành công"),
  UPDATE_SUCCESS("Cập nhật thông tin thành viên trong bảng thành công"),
  DELETE_SUCCESS("Xóa thành viên khỏi bảng thành công"),
  ALREADY_EXISTS("Thành viên đã tồn tại trong bảng");

  String message;
}
