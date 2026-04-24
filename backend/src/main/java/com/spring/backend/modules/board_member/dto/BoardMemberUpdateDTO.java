package com.spring.backend.modules.board_member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for updating a new board member")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardMemberUpdateDTO {
  @Schema(description = "Position of the board member in the board", example = "Chu nhiem")
  @NotBlank(message = "Vị trí không được để trống")
  String position;

  @Schema(
      description = "Additional description",
      example = "Hard-working and enthusiastic student.")
  String description;
}
