package com.spring.backend.modules.task.dto;

import com.spring.backend.modules.task.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for updating a new task")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskUpdateDTO {
  @Size(max = 255, message = "Title cannot exceed 255 characters")
  @NotBlank(message = "Title is required")
  @Schema(description = "Full title of the task", example = "Học kỳ 1 năm học 2023-2024")
  String title;

  @Schema(
      description = "Description of the task",
      example = "Kỳ học mùa thu năm 2023, nhiều hoạt động CLB")
  String description;

  @Schema(description = "Type of the task", example = "TASK")
  TaskType type;

  @Schema(description = "Date of the task", example = "2023-09-01T00:00:00Z")
  Instant date;

  @Schema(
      description = "Comma-separated list of member IDs associated with the task",
      example = "[ 1, 2, 3 ]")
  List<Long> memberIds;
}
