package com.spring.backend.modules.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "DTO for member statistics")
public class StatisticsMemberDTO {
  @Schema(description = "Total number of members")
  long totalMemberOfSemester;

  @Schema(description = "List of member counts by semester")
  List<MemberCountBySemesterDTO> memberCountBySemesters;

  @Getter
  @Builder
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class MemberCountBySemesterDTO {
    long semesterId;
    long totalMembers;
  }
}
