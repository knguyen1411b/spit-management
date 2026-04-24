package com.spring.backend.modules.user.dto;

import com.spring.backend.modules.role.dto.RoleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for User")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
  @Schema(description = "Unique identifier for the user", example = "1")
  Long id;

  @Schema(description = "Unique username of the user", example = "23T108")
  String username;

  @Schema(description = "Flag indicating whether the user account is enabled", example = "true")
  boolean enabled;

  @Schema(
      description = "Flag indicating whether the user is a superuser with full system access",
      example = "true")
  boolean superuser;

  @Schema(description = "Role of the user")
  List<RoleDTO> roles;

  @Schema(description = "Timestamp when the user was created", example = "2023-01-01T12:00:00Z")
  Instant createdAt;

  @Schema(
      description = "Timestamp when the user was last updated",
      example = "2023-01-01T12:00:00Z")
  Instant updatedAt;
}
