package com.spring.backend.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "DTO for changing user password")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordDTO {
  @Schema(description = "Current password of the user", example = "oldPassword123")
  @NotBlank(message = "Mât khẩu cũ không được để trống")
  String oldPassword;

  @Schema(description = "New password for the user", example = "newPassword123")
  @NotBlank(message = "Mât khẩu mới không được để trống")
  String newPassword;
}
