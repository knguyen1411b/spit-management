package com.spring.backend.modules.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for creating a new board")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardCreateDTO {
  @Schema(description = "Name of the board", example = "General Discussion")
  @Size(max = 255, message = "Tên ban không được vượt quá 255 ký tự")
  @NotBlank(message = "Tên ban không được để trống")
  String name;

  @Schema(
      description = "Description of the board",
      example = "A place for general topics and discussions")
  String description;
}
