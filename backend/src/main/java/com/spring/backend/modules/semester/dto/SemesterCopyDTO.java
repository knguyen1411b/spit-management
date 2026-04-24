package com.spring.backend.modules.semester.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for copying a semester")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemesterCopyDTO {
  @Schema(description = "Unique code of the semester", example = "KH1.2023-2024")
  @NotBlank(message = "Mã học kì là bắt buộc")
  String code;

  @Schema(description = "Name of the semester", example = "Học kì 1 năm học 2023-2024")
  @NotBlank(message = "Tên học kì là bắt buộc")
  String name;

  @Schema(
      description = "Description of the semester",
      example = "This semester focuses on advanced topics.")
  String description;

  @Schema(
      description = "Start date of the semester in ISO 8601 format",
      example = "2023-09-01T00:00:00Z")
  @NotNull(message = "Ngày bắt đầu học kì là bắt buộc")
  Instant startDate;

  @Schema(
      description = "End date of the semester in ISO 8601 format",
      example = "2024-01-31T23:59:59Z")
  @NotNull(message = "Ngày kết thúc học kì là bắt buộc")
  Instant endDate;

  @Schema(description = "Order of the semester within the academic year", example = "1")
  @NotNull(message = "Học kì thứ mấy là bắt buộc")
  Integer semesterOrder;

  @Schema(description = "Indicates if this is the current semester", example = "true")
  @NotNull(message = "Trạng thái học kì hiện tại là bắt buộc")
  Boolean current;

  @Schema(description = "ID of the semester to copy from", example = "1")
  @NotNull(message = "ID của học kì cần sao chép là bắt buộc")
  Long sourceSemesterId;

  @Schema(
      description = "Indicates whether to copy members from the source semester",
      example = "true",
      defaultValue = "true")
  Boolean copyMembers;

  @Schema(
      description = "Indicates whether to copy boards from the source semester",
      example = "true",
      defaultValue = "true")
  Boolean copyBoards;
}
