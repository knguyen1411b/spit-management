package com.spring.backend.modules.auth.dto;

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
@Schema(description = "Data Transfer Object for User Profile")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileDTO {
  @Schema(description = "Unique identifier of the user", example = "1")
  Long id;

  @Schema(description = "Email address of the user")
  String username;

  @Schema(description = "Last name of the user", example = "Nguyen")
  String lastName;

  @Schema(description = "First name of the user", example = "Van A")
  String firstName;

  @Schema(description = "Semester ID the user is enrolled in", example = "1")
  Long semesterId;

  @Schema(description = "Gender of the user (true = male, false = female)", example = "true")
  Boolean gender;

  @Schema(description = "Birthday of the user in ISO 8601 format", example = "2000-01-01T00:00:00Z")
  Instant birthday;

  @Schema(description = "Email of the user", example = "vana@example.com")
  String email;

  @Schema(description = "Phone number of the user", example = "0912345678")
  String phone;

  @Schema(description = "Class name", example = "SE1701")
  String className;

  @Schema(description = "Avatar URL", example = "uploads/avatars/default.png")
  String avatar;

  @Schema(description = "Generation of the user", example = "K17")
  String generation;

  @Schema(
      description = "Additional description",
      example = "Hard-working and enthusiastic student.")
  String description;

  @Schema(description = "List of permissions assigned to the user")
  List<String> permissions;

  @Schema(description = "Indicates if the user is enabled")
  Boolean enabled;

  @Schema(description = "Indicates if the user is a superuser")
  Boolean superuser;

  @Schema(description = "Indicates if the user has changed their password")
  Boolean passwordChanged;

  @Schema(description = "Timestamp when the user was created")
  Instant createdAt;

  @Schema(description = "Timestamp when the user was last updated")
  Instant updatedAt;
}
