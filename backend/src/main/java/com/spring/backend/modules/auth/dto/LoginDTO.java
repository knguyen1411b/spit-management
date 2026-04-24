package com.spring.backend.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for User Login")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDTO {
  @Schema(description = "Email address of the user", example = "admin")
  @NotBlank(message = "Tên đăng nhập không được để trống")
  String username;

  @Schema(description = "Password of the user", example = "admin")
  @NotBlank(message = "Mật khẩu không được để trống")
  String password;
}
