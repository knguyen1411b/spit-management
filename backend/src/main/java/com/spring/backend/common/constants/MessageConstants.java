package com.spring.backend.common.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public enum MessageConstants {
  CHECK_HEALTH("Hệ thống hoạt động"),
  INTERNAL_SERVER_ERROR("Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau"),
  BAD_REQUEST("Yêu cầu không hợp lệ, vui lòng kiểm tra lại"),
  FORBIDDEN("Bạn không có quyền truy cập tới tài nguyên này"),
  METHOD_ARGUMENT_TYPE_MISMATCH("Tham số truyền vào không hợp lệ"),
  UNAUTHORIZED("Bạn chưa đăng nhập hoặc phiên đăng nhập đã hết hạn"),
  NOT_FOUND("Tài nguyên không tồn tại hoặc không tìm thấy");

  String message;
}
