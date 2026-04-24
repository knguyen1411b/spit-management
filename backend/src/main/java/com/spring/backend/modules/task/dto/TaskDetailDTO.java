package com.spring.backend.modules.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Detailed Task DTO with associated members")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDetailDTO extends TaskDTO {
  List<TaskMemberDTO> members;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TaskMemberDTO {
    Long id;
    String lastName;
    String firstName;
    String email;
    String phone;
    String className;
    String avatar;
    String status;
    String description;
    Instant requestedAt;
    Instant approvedAt;
  }
}
