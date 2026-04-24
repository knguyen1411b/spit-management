package com.spring.backend.modules.board_member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for creating a new board member")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardMemberCreateDTO {
  @Schema(description = "Unique identifier of the member", example = "1")
  @NotNull(message = "Thành viên không được để trống")
  Long memberId;

  @Schema(description = "Position of the board member in the board", example = "Chu nhiem")
  @NotBlank(message = "Chức vụ không được để trống")
  String position;

  @Schema(
      description = "Additional description",
      example = "Hard-working and enthusiastic student.")
  String descriptionBoard;
}
