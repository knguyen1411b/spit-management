package com.spring.backend.modules.semester.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Semester DTO")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemesterDTO {
  @Schema(description = "Unique identifier for the semester", example = "1")
  Long id;

  @Schema(description = "Code name of the semester", example = "KH1.2023-2024")
  String code;

  @Schema(description = "Name of the semester", example = "Học kỳ 1 năm học 2023-2024")
  String name;

  @Schema(
      description = "Description of the semester",
      example = "Kỳ học mùa thu năm 2023, nhiều hoạt động CLB")
  String description;

  @Schema(description = "Start date of the semester", example = "2023-09-01T00:00:00Z")
  Instant startDate;

  @Schema(description = "End date of the semester", example = "2024-01-15T00:00:00Z")
  Instant endDate;

  @Schema(description = "Order of the semester in the year", example = "1")
  Integer semesterOrder;

  @Schema(description = "Is this the current semester?", example = "true")
  boolean current;

  @Schema(description = "Timestamp when the semester was created", example = "2023-01-01T12:00:00Z")
  Instant createdAt;

  @Schema(
      description = "Timestamp when the semester was last updated",
      example = "2023-01-02T15:30:00Z")
  Instant updatedAt;
}
