package com.spring.backend.modules.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Data Transfer Object for role")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTO {
  @Schema(description = "Unique identifier for the role", example = "1")
  Long id;

  @Schema(description = "Unique code of the role", example = "ADMIN")
  String code;

  @Schema(description = "Display name of the role", example = "Administrator")
  String name;

  @Schema(description = "Description of the role", example = "Full access to all system features")
  String description;

  @Schema(description = "Timestamp when the role was created", example = "2023-01-01T12:00:00Z")
  Instant createdAt;

  @Schema(
      description = "Timestamp when the role was last updated",
      example = "2023-01-02T15:30:00Z")
  Instant updatedAt;
}
