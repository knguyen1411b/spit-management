package com.spring.backend.modules.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Permission DTO")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionDTO {
  @Schema(description = "Unique identifier of the permission", example = "1")
  Long id;

  @Schema(description = "Code of the permission", example = "permission:read")
  String code;

  @Schema(description = "Name of the permission", example = "Read permissions")
  String name;

  @Schema(description = "Description of the permission", example = "Allows reading permissions")
  String description;

  @Schema(
      description = "Timestamp when the permission was created",
      example = "2023-01-01T12:00:00Z")
  Instant createdAt;

  @Schema(
      description = "Timestamp when the permission was last updated",
      example = "2023-01-01T12:00:00Z")
  Instant updatedAt;
}
