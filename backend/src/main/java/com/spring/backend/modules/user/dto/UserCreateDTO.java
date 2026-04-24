package com.spring.backend.modules.user.dto;

import com.spring.backend.common.validation.constraints.RolesExist;
import com.spring.backend.common.validation.constraints.UserNameNotExisted;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for creating a new user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDTO {
  @Schema(description = "Unique username of the user", example = "23T")
  @UserNameNotExisted
  @Size(min = 6, message = "Tên đăng nhập phải có ít nhất 6 ký tự")
  @NotBlank(message = "Tên đăng nhập không được để trống")
  String username;

  @Schema(description = "Password for the user account", example = "P@ssw0rd")
  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  @NotBlank(message = "Mật khẩu không được để trống")
  String password;

  @RolesExist
  @Schema(description = "List of role IDs assigned to the user", example = "[1, 2]")
  List<Long> roleIds;
}
