package com.spring.backend.modules.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data Transfer Object for Member")
public class MemberDTO {
  @Schema(description = "Unique identifier of the member", example = "1")
  Long id;

  @Schema(description = "Last name of the member", example = "Nguyen")
  String lastName;

  @Schema(description = "First name of the member", example = "Van A")
  String firstName;

  @Schema(description = "Gender of the member (true = male, false = female)", example = "true")
  Boolean gender;

  @Schema(
      description = "Birthday of the member in ISO 8601 format",
      example = "2000-01-01T00:00:00Z")
  Instant birthday;

  @Schema(description = "Email of the member", example = "vana@example.com")
  String email;

  @Schema(description = "Phone number of the member", example = "0912345678")
  String phone;

  @Schema(description = "Class name", example = "SE1701")
  String className;

  @Schema(description = "Avatar URL", example = "uploads/avatars/default.png")
  String avatar;

  @Schema(description = "Generation of the member", example = "K17")
  String generation;

  @Schema(
      description = "Additional description",
      example = "Hard-working and enthusiastic student.")
  String description;

  @Schema(description = "Username of the linked user", example = "23T")
  String username;

  @Schema(description = "Timestamp when the member was created", example = "2023-10-01T12:00:00Z")
  Instant createdAt;

  @Schema(
      description = "Timestamp when the member was last updated",
      example = "2023-10-15T15:30:00Z")
  Instant updatedAt;
}
