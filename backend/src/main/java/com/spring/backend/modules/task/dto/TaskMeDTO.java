package com.spring.backend.modules.task.dto;

import com.spring.backend.modules.task_request.TaskRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Data Transfer Object for task me")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskMeDTO {
  @Schema(description = "Task ID")
  Long id;

  @Schema(description = "Task title")
  String title;

  @Schema(description = "Task description")
  String description;

  @Schema(description = "Task date")
  Instant date;

  @Schema(description = "Task type")
  String type;

  @Schema(description = "Task member ID")
  Long taskMemberId;

  @Schema(description = "Task member status")
  TaskRequestStatus status;

  @Schema(description = "Timestamp when the task was requested")
  Instant requestedAt;

  @Schema(description = "Timestamp when the task was approved")
  Instant approvedAt;

  @Schema(description = "Task member description")
  String taskMemberDescription;
}
