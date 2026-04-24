package com.spring.backend.modules.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum BoardMessages {
  NOT_FOUND("Không tìm thấy ban"),
  CREATE_SUCCESS("Tạo ban thành công"),
  UPDATE_SUCCESS("Cập nhật ban thành công"),
  DELETE_SUCCESS("Xóa ban thành công"),
  INDEX_SUCCESS("Lấy danh sách ban thành công"),
  SHOW_SUCCESS("Lấy thông tin ban thành công");

  String message;
}
