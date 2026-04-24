package com.spring.backend.modules.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AuthMessages {
  LOGIN_SUCCESS("Đăng nhập thành công"),
  REFRESH_TOKEN_SUCCESS("Làm mới token thành công"),
  TOKEN_EXPIRED("Token đã hết hạn"),
  PROFILE_FETCH_SUCCESS("Lấy thông tin người dùng thành công"),
  USER_NOT_ENABLED("Tài khoản người dùng không được kích hoạt"),
  USER_NOT_LINK_MEMBER("Tài khoản người dùng chưa được liên kết với thành viên"),
  USER_LOGIN_FAILED("Đăng nhập thất bại. Vui lòng kiểm tra thông tin đăng nhập và thử lại"),
  INVALID_TOKEN("Token không hợp lệ hoặc đã hết hạn"),
  INVALID_OLD_PASSWORD("Mật khẩu cũ không chính xác. Vui lòng thử lại với mật khẩu đúng"),
  PASSWORD_CHANGE_SUCCESS("Đổi mật khẩu thành công. Bạn có thể đăng nhập bằng mật khẩu mới");
  String message;
}
