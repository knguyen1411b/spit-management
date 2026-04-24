package com.spring.backend.modules.task.dto;

import com.spring.backend.common.validation.constraints.MembersExist;
import com.spring.backend.modules.task.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for creating a new Task")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskCreateDTO {
  @Schema(description = "Full title of the task", example = "Học kỳ 1 năm học 2023-2024")
  @NotBlank(message = "Tiêu đề không được để trống")
  String title;

  @Schema(
      description = "Description of the task",
      example = "Kỳ học mùa thu năm 2023, nhiều hoạt động CLB")
  String description;

  @Schema(description = "Type of the task", example = "EVENT, TASK")
  @NotNull(message = "Loại công việc không được để trống")
  TaskType type;

  @Schema(description = "Date of the task", example = "2023-09-01T00:00:00Z")
  Instant date;

  @MembersExist
  @Schema(
      description = "Comma-separated list of member IDs associated with the task",
      example = "[ 1, 2, 3 ]")
  @NotNull(message = "Danh sách thành viên không được để trống")
  List<Long> memberIds;
}
