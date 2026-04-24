package com.spring.backend.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for updating the semester of an existing user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateSemesterDTO {
  @Schema(description = "ID of the semester associated with the user", example = "1")
  Long semesterId;
}
