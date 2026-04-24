package com.spring.backend.modules.task.dto;

import com.spring.backend.modules.task.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Task Filter Data Transfer Object")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskFilterDTO {
  @Schema(description = "Keyword to search in task name or title", example = "project")
  String keyword;

  @Schema(description = "Type of the task: EVENT, TASK, REMINDER, OTHER", example = "TASK")
  TaskType type;

  @Schema(description = "Start date for filtering tasks", example = "2023-01-01T00:00:00Z")
  Instant fromDate;

  @Schema(description = "End date for filtering tasks", example = "2023-12-31T23:59:59Z")
  Instant toDate;
}
