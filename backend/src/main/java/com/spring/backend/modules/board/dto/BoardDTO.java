package com.spring.backend.modules.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for Board entity")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardDTO {
  @Schema(description = "Unique identifier of the board", example = "1")
  Long id;

  @Schema(description = "Name of the board", example = "General Discussion")
  String name;

  @Schema(
      description = "Description of the board",
      example = "A place for general topics and discussions")
  String description;

  @Schema(description = "Timestamp when the board was created", example = "2023-01-01T12:00:00Z")
  Instant createdAt;

  @Schema(
      description = "Timestamp when the board was last updated",
      example = "2023-01-01T12:00:00Z")
  Instant updatedAt;
}
