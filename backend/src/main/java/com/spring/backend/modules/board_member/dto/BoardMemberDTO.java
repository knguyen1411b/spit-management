package com.spring.backend.modules.board_member.dto;

import com.spring.backend.modules.member.dto.MemberDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for Board Member entity")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardMemberDTO {
  @Schema(description = "Unique identifier of the board member", example = "1")
  Long id;

  @Schema(description = "Member details associated with the board member")
  MemberDTO member;

  @Schema(description = "Position of the board member in the board", example = "President")
  String position;

  @Schema(description = "Description of the board", example = "Student Council Board")
  String descriptionBoard;

  @Schema(
      description = "Timestamp when the board member was created",
      example = "2023-10-01T12:00:00Z")
  Instant createdAt;

  @Schema(
      description = "Timestamp when the board member was last updated",
      example = "2023-10-15T15:30:00Z")
  Instant updatedAt;
}
