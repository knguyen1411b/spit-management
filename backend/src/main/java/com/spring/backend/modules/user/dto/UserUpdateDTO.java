package com.spring.backend.modules.user.dto;

import com.spring.backend.common.validation.constraints.RolesExist;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for updating an existing user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateDTO {
  @Schema(description = "Password for the user account", example = "P@ssw0rd")
  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  String password;

  @RolesExist
  @Schema(description = "List of role IDs assigned to the user", example = "[1, 2]")
  List<Long> roleIds;

  @Schema(description = "Whether the user account is enabled", example = "true")
  Boolean enabled;
}
