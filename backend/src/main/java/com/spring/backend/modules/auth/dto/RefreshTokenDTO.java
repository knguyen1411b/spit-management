package com.spring.backend.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data transfer object for refresh tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenDTO {
  @Schema(
      description = "The refresh token for the user",
      example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  @NotBlank(message = "Refresh token không được để trống")
  String refreshToken;
}
