package com.spring.backend.modules.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Member Filter Data Transfer Object")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberFilterDTO {
  @Schema(description = "Full name of the member", example = "Van A")
  String fullName;

  @Schema(description = "Gender of the member (true = male, false = female)", example = "true")
  Boolean gender;

  @Schema(description = "Email of the member", example = "vana@example.com")
  String email;

  @Schema(description = "Phone number of the member", example = "0912345678")
  String phone;

  @Schema(description = "Class name", example = "SE1701")
  String className;

  @Schema(description = "Generation of the member", example = "K17")
  String generation;

  @Schema(
      description = "Additional description",
      example = "Hard-working and enthusiastic student.")
  String description;

  @Schema(description = "Username of the linked user", example = "23T")
  String username;

  @Schema(description = "Semester ID", example = "1")
  Long semesterId;
}
