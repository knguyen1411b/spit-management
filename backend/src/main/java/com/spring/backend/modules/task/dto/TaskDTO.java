package com.spring.backend.modules.task.dto;

import com.spring.backend.modules.task.TaskType;
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
@Schema(description = "Data Transfer Object for task")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDTO {
  @Schema(description = "Unique identifier for the task", example = "1")
  Long id;

  @Schema(description = "Full title of the task", example = "Công việc làm báo cáo")
  String title;

  @Schema(
      description = "Description of the task",
      example = "Khảo sát, thu thập số liệu và làm báo cáo")
  String description;

  @Schema(description = "Type of the task", example = "TASK")
  TaskType type;

  @Schema(description = "Date of the task", example = "2023-09-01T00:00:00Z")
  Instant date;

  @Schema(description = "Timestamp when the task was created", example = "2023-01-01T12:00:00Z")
  Instant createdAt;

  @Schema(
      description = "Timestamp when the task was last updated",
      example = "2023-01-02T15:30:00Z")
  Instant updatedAt;
}
