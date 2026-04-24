package com.spring.backend.modules.task_request.dto;

import com.spring.backend.modules.task_request.TaskRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for check request details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDTO {
  @Schema(description = "Unique identifier for the check request", example = "1")
  Long id;

  @Schema(description = "ID of the associated task", example = "10")
  Long taskId;

  @Schema(description = "ID of the member who made the check request", example = "5")
  Long memberId;

  @Schema(description = "Name of the associated task", example = "Implement Feature X")
  String taskTitle;

  @Schema(description = "Date of the check request", example = "2023-01-01T12:00:00Z")
  Instant taskDate;

  @Schema(description = "Name of the member who made the check request", example = "John Doe")
  String memberName;

  @Schema(description = "Status of the check request", example = "PENDING")
  TaskRequestStatus status;

  @Schema(
      description = "Description of the check request",
      example = "Please review my task submission.")
  String description;

  @Schema(
      description = "Timestamp when the check request was made",
      example = "2023-01-01T12:00:00Z")
  Instant requestAt;

  @Schema(
      description = "Timestamp when the check request was approved",
      example = "2023-01-02T12:00:00Z")
  Instant approveAt;

  @Schema(
      description = "Timestamp when the check request was created",
      example = "2023-01-01T12:00:00Z")
  Instant createdAt;

  @Schema(
      description = "Timestamp when the check request was last updated",
      example = "2023-01-02T15:30:00Z")
  Instant updatedAt;
}
