package com.spring.backend.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@Schema(description = "Data transfer object for authentication tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenDTO {
  @Schema(
      description = "The access token for the user",
      example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  String accessToken;

  @Schema(
      description = "The refresh token for the user",
      example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  String refreshToken;

  @Schema(description = "The expiration time of the access token", example = "3600")
  Long accessTokenExpiration;

  @Schema(description = "The expiration time of the refresh token", example = "86400")
  Long refreshTokenExpiration;
}
